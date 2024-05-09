package com.pbl.tasktoolintegration.monday;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.tasktoolintegration.monday.entity.MondayAssignees;
import com.pbl.tasktoolintegration.monday.entity.MondayBoards;
import com.pbl.tasktoolintegration.monday.entity.MondayComments;
import com.pbl.tasktoolintegration.monday.entity.MondayConfigurations;
import com.pbl.tasktoolintegration.monday.entity.MondayConfigurationsBoards;
import com.pbl.tasktoolintegration.monday.entity.MondayConfigurationsUsers;
import com.pbl.tasktoolintegration.monday.entity.MondayItems;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdates;
import com.pbl.tasktoolintegration.monday.entity.MondayUsers;
import com.pbl.tasktoolintegration.monday.model.GetUserExpiredItemDto;
import com.pbl.tasktoolintegration.monday.model.GetUsersAverageResponseTimeDto;
import com.pbl.tasktoolintegration.monday.model.MondayAssigneeInfo;
import com.pbl.tasktoolintegration.monday.model.MondayStatusColumnInfo;
import com.pbl.tasktoolintegration.monday.model.MondayStatusInfo;
import com.pbl.tasktoolintegration.monday.model.MondayTimelineColumnInfo;
import com.pbl.tasktoolintegration.monday.model.GetAllMondayBoardsDto;
import com.pbl.tasktoolintegration.monday.model.GetAllMondayItemsWIthCursorRes;
import com.pbl.tasktoolintegration.monday.model.GetAllMondayUsersDto;
import com.pbl.tasktoolintegration.monday.model.MondayGetAllBoardsRes;
import com.pbl.tasktoolintegration.monday.model.MondayGetAllUpdatesWithCommentRes;
import com.pbl.tasktoolintegration.monday.model.MondayGetAllUsersRes;
import com.pbl.tasktoolintegration.monday.repository.MondayAssigneesRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayBoardsRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayCommentsRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayConfigurationsBoardsRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayConfigurationsRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayConfigurationsUsersRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayItemsRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayUpdatesRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayUsersRepository;
import graphql.kickstart.spring.webclient.boot.GraphQLRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MondayService {

    private final WebClient mondayWebClient;
    private final ObjectMapper objectMapper;
    private final MondayUsersRepository mondayUsersRepository;
    private final MondayConfigurationsRepository mondayConfigurationsRepository;
    private final MondayBoardsRepository mondayBoardsRepository;
    private final MondayConfigurationsUsersRepository mondayConfigurationsUsersRepository;
    private final MondayConfigurationsBoardsRepository mondayConfigurationsBoardsRepository;
    private final MondayItemsRepository mondayItemsRepository;
    private final MondayAssigneesRepository mondayAssigneesRepository;
    private final MondayCommentsRepository mondayCommentsRepository;
    private final MondayUpdatesRepository mondayUpdatesRepository;

    private List<String> getMentionedUsersInString(String text) {
        List<String> mentionedUsers = new ArrayList<>();
        String[] words = text.split("\\s+|\\n");
        for (String word : words) {
            if (!word.isEmpty() && word.charAt(0) == '@'
                && !mentionedUsers.contains(word.substring(1))) {
                mentionedUsers.add(word.substring(1));
            }
        }
        return mentionedUsers;
    }

    private int calculateResponseTime(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return (int) (diff / 1000);
    }

    public List<GetUsersAverageResponseTimeDto> getUsersAverageResponseTime(Long id) {
        MondayConfigurations config = mondayConfigurationsRepository.findById(id).get();
        List<MondayUsers> mondayUsers = mondayConfigurationsUsersRepository.findByMondayConfiguration(config).stream()
            .map(MondayConfigurationsUsers::getMondayUser)
            .collect(Collectors.toList());
        List<String> mondayBoardIds = mondayConfigurationsBoardsRepository.findByMondayConfiguration(config).stream()
            .map(MondayConfigurationsBoards::getMondayBoard)
            .map(MondayBoards::getId)
            .collect(Collectors.toList());

        Map<String, List<Integer>> userResponseTimeMap = new HashMap<>();
        for (MondayUsers user : mondayUsers) {
            userResponseTimeMap.put(user.getName(), new ArrayList<>());
        }

        List<MondayUpdates> mondayUpdates = mondayUpdatesRepository.findByMondayBoardId(mondayBoardIds);

        for (MondayUpdates update : mondayUpdates) {
            List<String> mentionedUsers = getMentionedUsersInString(update.getContent());
            if (!mentionedUsers.isEmpty()) {
                Date startDate = update.getCreatedAt();
                List<String> leftUsers = new ArrayList<>(mentionedUsers);
                List<MondayComments> mondayComments = mondayCommentsRepository.findByMondayUpdate(update);
                for (MondayComments comment : mondayComments) {
                    if (mentionedUsers.contains(comment.getMondayCreatorUser().getName()) && leftUsers.contains(comment.getMondayCreatorUser().getName())){
                        int diffSeconds = calculateResponseTime(startDate, comment.getCreatedAt());
                        userResponseTimeMap.get(comment.getMondayCreatorUser().getName()).add(diffSeconds);
                        leftUsers.remove(comment.getMondayCreatorUser().getName());
                    }
                }
            }
        }

        List<GetUsersAverageResponseTimeDto> usersAverageResponseTime = new ArrayList<>();
        for (String userId : userResponseTimeMap.keySet()) {
            List<Integer> responseTimes = userResponseTimeMap.get(userId);
            // 응답한 기록이 없는 경우 분석 불가
            if (responseTimes.isEmpty()) {
                continue;
            }

            int sum = 0;
            for (int responseTime : responseTimes) {
                sum += responseTime;
            }
            int average = sum / responseTimes.size();
            usersAverageResponseTime.add(GetUsersAverageResponseTimeDto.builder()
                .username(userId)
                .averageResponseTime(average)
                .build());
        }
        return usersAverageResponseTime;
    }

    public List<GetUserExpiredItemDto> getUsersExpiredItem(Long configId) {
        List<MondayUsers> mondayUsers = mondayConfigurationsUsersRepository.findByMondayConfiguration(mondayConfigurationsRepository.findById(configId).get()).stream()
            .map(MondayConfigurationsUsers::getMondayUser)
            .collect(Collectors.toList());
        Map<String, Integer > userExpiredItemCount = new HashMap<>();

        for (MondayUsers user : mondayUsers) {
            List<MondayItems> mondayItems = mondayAssigneesRepository.findByMondayUserAndMondayItem_StatusAndMondayItem_DeadlineToLessThan(user, "미완료", new Date()).stream()
                .map(MondayAssignees::getMondayItem)
                .collect(Collectors.toList());

            userExpiredItemCount.put(user.getName(), mondayItems.size());
        }


        return userExpiredItemCount.entrySet().stream()
            .map(entry -> GetUserExpiredItemDto.builder()
                .username(entry.getKey())
                .totalExpiredItems(entry.getValue())
                .build())
            .collect(Collectors.toList());
    }

    public List<GetAllMondayUsersDto> getAllMondayUsers(String apiKey) {
        GraphQLRequest userRequest = GraphQLRequest.builder()
            .query(ModnayQuery.MONDAY_GET_ALL_USERS.getQuery())
            .build();
        MondayGetAllUsersRes response = mondayWebClient.post()
            .bodyValue(userRequest.getRequestBody())
            .header("Authorization", apiKey)
            .retrieve()
            .bodyToMono(MondayGetAllUsersRes.class)
            .block();

        return response.getData().getUsers().stream()
            .map(GetAllMondayUsersDto::from)
            .collect(Collectors.toList());
    }

    public List<GetAllMondayBoardsDto> getAllMondayBoards(String apiKey) {
        List<GetAllMondayBoardsDto> response = new ArrayList<>();
        int page = 1;
        while (true) {
            GraphQLRequest boardRequest = GraphQLRequest.builder()
                .query(String.format(ModnayQuery.MONDAY_GET_ALL_BOARDS.getQuery(), page))
                .build();
            MondayGetAllBoardsRes res = mondayWebClient.post()
                .bodyValue(boardRequest.getRequestBody())
                .header("Authorization", apiKey)
                .retrieve()
                .bodyToMono(MondayGetAllBoardsRes.class)
                .doOnError(e -> log.error("error : {}", e.getMessage()))
                .block();

            if (res.getData().getBoards().size() == 0) {
                break;
            }

            response.addAll(res.getData().getBoards().stream()
                .map(GetAllMondayBoardsDto::from)
                .collect(Collectors.toList()));
            page++;
        }

        return response;
    }

    public void syncUsers(List<Long> mondayConfigurationIds) {
        for (Long id : mondayConfigurationIds) {
            MondayConfigurations mondayConfiguration = mondayConfigurationsRepository.findById(id)
                .get();
            List<GetAllMondayUsersDto> mondayUsers = getAllMondayUsers(mondayConfiguration.getApiKey());
            for (GetAllMondayUsersDto user : mondayUsers) {
                MondayUsers savedUser = mondayUsersRepository.save(MondayUsers.builder()
                    .id(user.getId())
                    .createdAt(user.getCreatedAt())
                    .email(user.getEmail())
                    .name(user.getName())
                    .phoneNumber(user.getPhoneNumber())
                    .title(user.getTitle())
                    .build());
                mondayConfigurationsUsersRepository.save(MondayConfigurationsUsers.builder()
                    .mondayConfiguration(mondayConfiguration)
                    .mondayUser(savedUser)
                    .build());
            }
        }
    }

    public List<Long> getMondayConfigIds() {
        return mondayConfigurationsRepository.findAll().stream()
            .map(MondayConfigurations::getId)
            .collect(Collectors.toList());
    }

    public void syncItemsByBoardId(String boardId, String apiKey) throws JsonProcessingException {
        MondayBoards board = mondayBoardsRepository.findById(boardId)
            .get();
        String cursor = null;
        do {
            GraphQLRequest itemsRequest = cursor == null ? GraphQLRequest.builder()
                .query(String.format(ModnayQuery.MONDAY_GET_ALL_ITEMS_BY_BOARDS_WITHOUT_CURSOR.getQuery(), boardId))
                .build() : GraphQLRequest.builder()
                .query(String.format(ModnayQuery.MONDAY_GET_ALL_ITEMS_BY_BOARDS_WITH_CURSOR.getQuery(), boardId, cursor))
                .build();
            GetAllMondayItemsWIthCursorRes items = mondayWebClient.post()
                .bodyValue(itemsRequest.getRequestBody())
                .header("Authorization", apiKey)
                .retrieve()
                .bodyToMono(GetAllMondayItemsWIthCursorRes.class)
                .block();

            for (GetAllMondayItemsWIthCursorRes.Item item : items.getData().getBoards().get(0).getItems_page().getItems()) {
                MondayItems savedItem = mondayItemsRepository.save(MondayItems.builder()
                    .id(item.getId())
                    .mondayBoard(board)
                    .createdAt(item.getCreated_at())
                    .updatedAt(item.getUpdated_at())
                    .name(item.getName())
                    .build());

                for (GetAllMondayItemsWIthCursorRes.ColumnValue columnValue : item.getColumn_values()) {
                    if (columnValue.getColumn().getTitle().equals("작업자") && columnValue.getValue() != null){
                        MondayAssigneeInfo assigneeInfo = objectMapper.readValue(columnValue.getValue(),
                            MondayAssigneeInfo.class);

                        for (MondayAssigneeInfo.Person person : assigneeInfo.getPersonsAndTeams()) {
                            MondayUsers assignee = mondayUsersRepository.findById(person.getId().toString()).get();
                            mondayAssigneesRepository.save(MondayAssignees.builder()
                                .mondayItem(savedItem)
                                .mondayUser(assignee)
                                .build());
                        }

                        continue;
                    }

                    if (columnValue.getColumn().getTitle().equals("타임라인") && columnValue.getValue() != null){
                        MondayTimelineColumnInfo deadline = objectMapper.readValue(columnValue.getValue(),
                            MondayTimelineColumnInfo.class);
                        mondayItemsRepository.save(savedItem.updateDeadline(deadline.getFrom(), deadline.getTo()));
                        continue;
                    }

                    if (columnValue.getColumn().getTitle().equals("상태") && columnValue.getValue() != null){
                        MondayStatusColumnInfo status = objectMapper.readValue(columnValue.getValue(),
                            MondayStatusColumnInfo.class);
                        Integer index = status.getIndex();

                        MondayStatusInfo statusInfo = objectMapper.readValue(columnValue.getColumn().getSettings_str(),
                            MondayStatusInfo.class);

                        if (statusInfo.getLabels().get(index.toString()) != null && statusInfo.getLabels().get(index.toString()).equals("진행완료")) {
                            mondayItemsRepository.save(savedItem.updateStatus("완료"));
                        } else {
                            mondayItemsRepository.save(savedItem.updateStatus("미완료"));
                        }
                    }
                }
            }

            cursor = items.getData().getBoards().get(0).getItems_page().getCursor();
        } while (cursor != null);
    }

    public void syncBoardsWithItems(List<Long> mondayConfigurationIds)
        throws JsonProcessingException {
        for (Long id : mondayConfigurationIds) {
            MondayConfigurations mondayConfiguration = mondayConfigurationsRepository.findById(id)
                .get();
            List<GetAllMondayBoardsDto> mondayBoards = getAllMondayBoards(mondayConfiguration.getApiKey());
            for (GetAllMondayBoardsDto board : mondayBoards) {
                MondayBoards savedBoard = mondayBoardsRepository.save(MondayBoards.builder()
                    .id(board.getId())
                    .name(board.getName())
                    .updatedAt(board.getUpdatedAt())
                    .build());
                mondayConfigurationsBoardsRepository.save(MondayConfigurationsBoards.builder()
                    .mondayConfiguration(mondayConfiguration)
                    .mondayBoard(savedBoard)
                    .build());

                syncItemsByBoardId(savedBoard.getId(), mondayConfiguration.getApiKey());
            }
        }
    }

    public void syncUpdatesAndComments(List<Long> mondayConfigurationIds) {
        for (Long id : mondayConfigurationIds) {
            MondayConfigurations mondayConfiguration = mondayConfigurationsRepository.findById(id)
                .get();
            String apiKey = mondayConfiguration.getApiKey();

            int page = 1;
            while (true) {
                GraphQLRequest updateRequest = GraphQLRequest.builder()
                    .query(String.format(ModnayQuery.MODNAY_GET_ALL_UPDDATES_WITH_COMMENTS_PAGE.getQuery(), page))
                    .build();
                MondayGetAllUpdatesWithCommentRes updates = mondayWebClient.post()
                    .bodyValue(updateRequest.getRequestBody())
                    .header("Authorization", apiKey)
                    .retrieve()
                    .bodyToMono(MondayGetAllUpdatesWithCommentRes.class)
                    .block();

                if (updates.getData().getUpdates().size() == 0) {
                    break;
                }

                for (MondayGetAllUpdatesWithCommentRes.Update update : updates.getData().getUpdates()) {
                    MondayItems item = mondayItemsRepository.findByUniqueId(update.getItem_id()).get(0);
                    MondayUpdates savedUpdate = mondayUpdatesRepository.save(MondayUpdates.builder()
                        .id(update.getId())
                        .mondayItem(item)
                        .mondayCreatorUser(mondayUsersRepository.findById(update.getCreator_id()).get())
                        .createdAt(update.getCreated_at())
                        .content(update.getText_body())
                        .updatedAt(update.getUpdated_at())
                        .build());

                    for (MondayGetAllUpdatesWithCommentRes.Reply reply : update.getReplies()) {
                        mondayCommentsRepository.save(MondayComments.builder()
                            .id(reply.getId())
                            .mondayUpdate(savedUpdate)
                            .mondayCreatorUser(
                                mondayUsersRepository.findById(reply.getCreator_id()).get())
                            .createdAt(reply.getCreated_at())
                            .updatedAt(reply.getUpdated_at())
                            .content(reply.getText_body())
                            .build());
                    }
                }
                page++;
            }
        }
    }
}