package com.pbl.tasktoolintegration.monday;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.tasktoolintegration.monday.entity.MondayAssignees;
import com.pbl.tasktoolintegration.monday.entity.MondayAvgResponseTimeEachUser;
import com.pbl.tasktoolintegration.monday.entity.MondayBoards;
import com.pbl.tasktoolintegration.monday.entity.MondayBoardsSubscribers;
import com.pbl.tasktoolintegration.monday.entity.MondayCommentHistory;
import com.pbl.tasktoolintegration.monday.entity.MondayComments;
import com.pbl.tasktoolintegration.monday.entity.MondayConfigurations;
import com.pbl.tasktoolintegration.monday.entity.MondayConfigurationsBoards;
import com.pbl.tasktoolintegration.monday.entity.MondayConfigurationsUsers;
import com.pbl.tasktoolintegration.monday.entity.MondayItems;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdateHistory;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdates;
import com.pbl.tasktoolintegration.monday.entity.MondayUsers;
import com.pbl.tasktoolintegration.monday.entity.ResponseTimeType;
import com.pbl.tasktoolintegration.monday.model.ActionWebhookDto;
import com.pbl.tasktoolintegration.monday.model.GetUserExpiredItemDto;
import com.pbl.tasktoolintegration.monday.model.GetUserNumberOfChangesDto;
import com.pbl.tasktoolintegration.monday.model.GetUsersAverageResponseTimeDto;
import com.pbl.tasktoolintegration.monday.model.GetUsersAverageResponseTimeDto.ResponseTimeOfUser;
import com.pbl.tasktoolintegration.monday.model.MondayAssigneeInfo;
import com.pbl.tasktoolintegration.monday.model.MondayGetUpdateByIdRes;
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
import com.pbl.tasktoolintegration.monday.repository.MondayAvgResponseTimeEachUserRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayBoardsRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayBoardsSubscribersRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayCommentHistoryRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayCommentsRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayConfigurationsBoardsRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayConfigurationsRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayConfigurationsUsersRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayItemsRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayUpdateHistoryRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayUpdatesRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayUsersRepository;
import graphql.kickstart.spring.webclient.boot.GraphQLRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    private final MondayBoardsSubscribersRepository mondayBoardsSubscribersRepository;
    private final MondayAvgResponseTimeEachUserRepository mondayAvgResponseTimeEachUserRepository;
    private final MondayUpdateHistoryRepository mondayUpdateHistoryRepository;
    private final MondayCommentHistoryRepository mondayCommentHistoryRepository;

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

    public List<GetUsersAverageResponseTimeDto> getUsersAverageResponseTime(Long id, ResponseTimeType period) {
        MondayConfigurations config = mondayConfigurationsRepository.findById(id).get();
        List<GetUsersAverageResponseTimeDto> usersAverageResponseTime = new ArrayList<>();

        List<String> mondayBoardIds = mondayConfigurationsBoardsRepository.findByMondayConfiguration(config).stream()
            .map(MondayConfigurationsBoards::getMondayBoard)
            .map(MondayBoards::getId)
            .collect(Collectors.toList());

        for (String boardId : mondayBoardIds) {
            List<MondayUsers> mondayUsers = mondayBoardsSubscribersRepository.findByMondayBoardId(boardId).stream()
                .map(MondayBoardsSubscribers::getMondayUser)
                .collect(Collectors.toList());

            MondayBoards mondayBoard = mondayBoardsRepository.findById(boardId).get();

            Map<String, List<Integer>> userResponseTimeMap = new HashMap<>();
            Map<String, String> userNameIdMap = new HashMap<>();
            for (MondayUsers user : mondayUsers) {
                userResponseTimeMap.put(user.getName(), new ArrayList<>());
                userNameIdMap.put(user.getName(), user.getId());
            }

            List<MondayUpdates> mondayUpdates;
            if (period.equals(ResponseTimeType.DAILY)) {
                mondayUpdates = mondayUpdatesRepository.findByMondayItem_MondayBoardIdAndCreatedAtAfter(boardId,
                    Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
            } else if (period.equals(ResponseTimeType.WEEKLY)) {
                mondayUpdates = mondayUpdatesRepository.findByMondayItem_MondayBoardIdAndCreatedAtAfter(boardId,
                    Timestamp.valueOf(LocalDateTime.now().minusDays(7)));
            } else {
                // default monthly
                mondayUpdates = mondayUpdatesRepository.findByMondayItem_MondayBoardIdAndCreatedAtAfter(boardId,
                    Timestamp.valueOf(LocalDateTime.now().minusDays(30)));
            }

            for (MondayUpdates update : mondayUpdates) {
                List<String> mentionedUsers = getMentionedUsersInString(update.getContent());
                if (!mentionedUsers.isEmpty()) {
                    Date startDate = update.getCreatedAt();
                    List<String> leftUsers = new ArrayList<>(mentionedUsers);
                    List<MondayComments> mondayComments = mondayCommentsRepository.findByMondayUpdate(update);
                    for (MondayComments comment : mondayComments) {
                        if (mentionedUsers.contains(comment.getMondayCreatorUser().getName()) && leftUsers.contains(comment.getMondayCreatorUser().getName())){
                            int diffSeconds = calculateResponseTime(startDate, comment.getCreatedAt());

                            // 현재 삭제된 유저의 경우에는 현재 구독자로 남아있지 않으며 언급은 되어있을 수 있기 때문에 체크
                            if (userResponseTimeMap.containsKey(comment.getMondayCreatorUser().getName())) {
                                userResponseTimeMap.get(comment.getMondayCreatorUser().getName()).add(diffSeconds);
                                leftUsers.remove(comment.getMondayCreatorUser().getName());
                            }
                        }
                    }
                }
            }

            List<ResponseTimeOfUser> responseTimeOfUsers = new ArrayList<>();

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
                responseTimeOfUsers.add(ResponseTimeOfUser.builder()
                    .userId(userNameIdMap.get(userId))
                    .username(userId)
                    .averageResponseTime(average)
                    .build());
            }

            usersAverageResponseTime.add(GetUsersAverageResponseTimeDto.builder()
                .boardId(mondayBoard.getId())
                .boardName(mondayBoard.getName())
                .responseTimeOfUsers(responseTimeOfUsers)
                .build());
        }

        return usersAverageResponseTime;
    }

    public List<GetUsersAverageResponseTimeDto> getUsersAverageResponseTime(Long id) {
        MondayConfigurations config = mondayConfigurationsRepository.findById(id).get();
        List<GetUsersAverageResponseTimeDto> usersAverageResponseTime = new ArrayList<>();

        List<String> mondayBoardIds = mondayConfigurationsBoardsRepository.findByMondayConfiguration(config).stream()
            .map(MondayConfigurationsBoards::getMondayBoard)
            .map(MondayBoards::getId)
            .collect(Collectors.toList());

        for (String boardId : mondayBoardIds) {
            List<MondayUsers> mondayUsers = mondayBoardsSubscribersRepository.findByMondayBoardId(boardId).stream()
                .map(MondayBoardsSubscribers::getMondayUser)
                .collect(Collectors.toList());

            MondayBoards mondayBoard = mondayBoardsRepository.findById(boardId).get();

            Map<String, List<Integer>> userResponseTimeMap = new HashMap<>();
            Map<String, String> userNameIdMap = new HashMap<>();
            for (MondayUsers user : mondayUsers) {
                userResponseTimeMap.put(user.getName(), new ArrayList<>());
                userNameIdMap.put(user.getName(), user.getId());
            }

            List<MondayUpdates> mondayUpdates = mondayUpdatesRepository.findByMondayBoardId(boardId);

            for (MondayUpdates update : mondayUpdates) {
                List<String> mentionedUsers = getMentionedUsersInString(update.getContent());
                if (!mentionedUsers.isEmpty()) {
                    Date startDate = update.getCreatedAt();
                    List<String> leftUsers = new ArrayList<>(mentionedUsers);
                    List<MondayComments> mondayComments = mondayCommentsRepository.findByMondayUpdate(update);
                    for (MondayComments comment : mondayComments) {
                        if (mentionedUsers.contains(comment.getMondayCreatorUser().getName()) && leftUsers.contains(comment.getMondayCreatorUser().getName())){
                            int diffSeconds = calculateResponseTime(startDate, comment.getCreatedAt());

                            // 현재 삭제된 유저의 경우에는 현재 구독자로 남아있지 않으며 언급은 되어있을 수 있기 때문에 체크
                            if (userResponseTimeMap.containsKey(comment.getMondayCreatorUser().getName())) {
                                userResponseTimeMap.get(comment.getMondayCreatorUser().getName()).add(diffSeconds);
                                leftUsers.remove(comment.getMondayCreatorUser().getName());
                            }
                        }
                    }
                }
            }

            List<ResponseTimeOfUser> responseTimeOfUsers = new ArrayList<>();

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
                responseTimeOfUsers.add(ResponseTimeOfUser.builder()
                    .userId(userNameIdMap.get(userId))
                    .username(userId)
                    .averageResponseTime(average)
                    .build());
            }

            usersAverageResponseTime.add(GetUsersAverageResponseTimeDto.builder()
                .boardId(mondayBoard.getId())
                .boardName(mondayBoard.getName())
                .responseTimeOfUsers(responseTimeOfUsers)
                .build());
        }

        return usersAverageResponseTime;
    }

    public List<GetUserExpiredItemDto> getUsersExpiredItem(Long configId) {
        List<MondayBoards> boards = mondayConfigurationsBoardsRepository.findByMondayConfiguration(mondayConfigurationsRepository.findById(configId).get()).stream()
            .map(MondayConfigurationsBoards::getMondayBoard)
            .collect(Collectors.toList());

        List<GetUserExpiredItemDto> userExpiredItems = new ArrayList<>();

        // 하위 아이템만 필터링 -> monday는 하위 아이템 묶음을 보드로 관리하기 때문에 이름으로 필터링
        for (MondayBoards board : boards) {
            if (!board.getName().contains("하위 아이템")) {
                continue;
            }
            List<MondayUsers> mondayUsers = mondayBoardsSubscribersRepository.findByMondayBoardId(
                    board.getId()).stream()
                .map(MondayBoardsSubscribers::getMondayUser)
                .collect(Collectors.toList());

            Map<String, Integer > userExpiredItemCount = new HashMap<>();

            for (MondayUsers user : mondayUsers) {
                List<MondayItems> mondayItems = mondayAssigneesRepository.findByMondayUserAndMondayItem_StatusAndMondayItem_DeadlineToLessThanAndMondayItem_MondayBoard(user, "미완료", new Date(), board).stream()
                    .map(MondayAssignees::getMondayItem)
                    .collect(Collectors.toList());

                userExpiredItemCount.put(user.getName(), mondayItems.size());
            }

            userExpiredItems.add(GetUserExpiredItemDto.builder()
                .boardName(board.getName())
                .expiredItemsOfUsers(userExpiredItemCount.entrySet().stream()
                    .map(entry -> GetUserExpiredItemDto.ExpiredItemsOfUser.builder()
                        .username(entry.getKey())
                        .totalExpiredItems(entry.getValue())
                        .build())
                    .collect(Collectors.toList()))
                .build());
        }


        return userExpiredItems;
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

    public void syncUsers(Long id) {
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

    public void syncBoardsWithItems(Long id)
        throws JsonProcessingException {
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

            List<MondayUsers> users = mondayUsersRepository.findByIdIn(board.getSubscriberIds());
            for (MondayUsers user : users) {
                mondayBoardsSubscribersRepository.save(MondayBoardsSubscribers.builder()
                    .mondayBoard(savedBoard)
                    .mondayUser(user)
                    .build());
            }

            syncItemsByBoardId(savedBoard.getId(), mondayConfiguration.getApiKey());
        }
    }

    public void syncUpdatesAndComments(Long id) {
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

    public void actionWebhook(ActionWebhookDto actionWebhookDto) {
        switch (actionWebhookDto.getType()) {
            case "create_update": case "edit_update":
                MondayConfigurations mondayConfiguration = mondayConfigurationsBoardsRepository.findByMondayBoard_Id(actionWebhookDto.getBoardId()).get(0).getMondayConfiguration();
                patchUpdateByUpdateId(mondayConfiguration.getApiKey(), actionWebhookDto.getUpdateId());
                break;
        }
    }

    public void patchUpdateByUpdateId(String apiKey, String id) {
        GraphQLRequest updateRequest = GraphQLRequest.builder()
            .query(String.format(ModnayQuery.MONDAY_GET_UPDATE_BY_ID.getQuery(), id))
            .build();
        MondayGetUpdateByIdRes update = mondayWebClient.post()
            .bodyValue(updateRequest.getRequestBody())
            .header("Authorization", apiKey)
            .retrieve()
            .bodyToMono(MondayGetUpdateByIdRes.class)
            .block();
        MondayItems item = mondayItemsRepository.findByUniqueId(update.getData().getUpdates().get(0).getItem_id()).get(0);
        MondayUpdates savedUpdate = mondayUpdatesRepository.save(MondayUpdates.builder()
            .id(id)
            .mondayItem(item)
            .mondayCreatorUser(mondayUsersRepository.findById(update.getData().getUpdates().get(0).getCreator_id()).get())
            .createdAt(update.getData().getUpdates().get(0).getCreated_at())
            .content(update.getData().getUpdates().get(0).getText_body())
            .updatedAt(update.getData().getUpdates().get(0).getUpdated_at())
            .build());

        for (MondayGetUpdateByIdRes.Reply reply : update.getData().getUpdates().get(0).getReplies()) {
            mondayCommentsRepository.save(MondayComments.builder()
                .id(reply.getId())
                .mondayUpdate(savedUpdate)
                .mondayCreatorUser(mondayUsersRepository.findById(reply.getCreator_id()).get())
                .createdAt(reply.getCreated_at())
                .updatedAt(reply.getUpdated_at())
                .content(reply.getText_body())
                .build());
        }
    }

    public void syncAvgResponseTimeEachUser(List<Long> mondayConfigIds, ResponseTimeType period) {
        for (Long id : mondayConfigIds) {
            List<GetUsersAverageResponseTimeDto> usersAverageResponseTime = getUsersAverageResponseTime(id, period);
            for (GetUsersAverageResponseTimeDto dto : usersAverageResponseTime) {
                MondayBoards board = mondayBoardsRepository.findById(dto.getBoardId()).get();
                for (ResponseTimeOfUser responseTimeOfUser : dto.getResponseTimeOfUsers()) {
                    MondayUsers user = mondayUsersRepository.findById(responseTimeOfUser.getUserId()).get();
                    MondayAvgResponseTimeEachUser responseTime = MondayAvgResponseTimeEachUser.builder()
                        .mondayUser(user)
                        .mondayBoard(board)
                        .type(period.name())
                        .avgResponseTime(responseTimeOfUser.getAverageResponseTime())
                        .build();

                    mondayAvgResponseTimeEachUserRepository.save(responseTime);
                }
            }
        }
    }

    public List<GetUsersAverageResponseTimeDto> getUsersAverageResponseTimePeriod(Long id, ResponseTimeType type) {
        MondayConfigurations config = mondayConfigurationsRepository.findById(id).get();
        List<GetUsersAverageResponseTimeDto> usersAverageResponseTime = new ArrayList<>();

        List<String> mondayBoardIds = mondayConfigurationsBoardsRepository.findByMondayConfiguration(config).stream()
            .map(MondayConfigurationsBoards::getMondayBoard)
            .map(MondayBoards::getId)
            .collect(Collectors.toList());

        for (String boardId : mondayBoardIds) {
            List<MondayAvgResponseTimeEachUser> avgResponseTimes = mondayAvgResponseTimeEachUserRepository.findByMondayBoardIdAndType(boardId, type.name());
            List<ResponseTimeOfUser> responseTimeOfUsers = avgResponseTimes.stream()
                .map(avgResponseTime -> ResponseTimeOfUser.builder()
                    .userId(avgResponseTime.getMondayUser().getId())
                    .username(avgResponseTime.getMondayUser().getName())
                    .averageResponseTime(avgResponseTime.getAvgResponseTime())
                    .build())
                .collect(Collectors.toList());

            MondayBoards mondayBoard = mondayBoardsRepository.findById(boardId).get();
            usersAverageResponseTime.add(GetUsersAverageResponseTimeDto.builder()
                .boardId(mondayBoard.getId())
                .boardName(mondayBoard.getName())
                .responseTimeOfUsers(responseTimeOfUsers)
                .build());
        }

        return usersAverageResponseTime;
    }

    public List<GetUserNumberOfChangesDto> getUsersNumberOfChanges(Long id) {
        MondayConfigurations config = mondayConfigurationsRepository.findById(id).get();
        List<GetUserNumberOfChangesDto> usersNumberOfChanges = new ArrayList<>();

        List<String> mondayBoardIds = mondayConfigurationsBoardsRepository.findByMondayConfiguration(config).stream()
            .map(MondayConfigurationsBoards::getMondayBoard)
            .map(MondayBoards::getId)
            .collect(Collectors.toList());

        for (String boardId : mondayBoardIds) {
            List<MondayUsers> mondayUsers = mondayBoardsSubscribersRepository.findByMondayBoardId(boardId).stream()
                .map(MondayBoardsSubscribers::getMondayUser)
                .collect(Collectors.toList());

            MondayBoards mondayBoard = mondayBoardsRepository.findById(boardId).get();

            Map<String, Integer> userChangeCount = new HashMap<>();
            for (MondayUsers user : mondayUsers) {
                userChangeCount.put(user.getName(), 0);
            }

            List<MondayUpdates> mondayUpdates = mondayUpdatesRepository.findByMondayBoardId(boardId);

           for (MondayUpdates update : mondayUpdates) {
                MondayUsers creator = update.getMondayCreatorUser();

                List<MondayUpdateHistory> histories = mondayUpdateHistoryRepository.findByMondayUpdateId(update.getId());

                for (int i = 0; i < histories.size()-1; i++) {
                    if (!histories.get(i).getContent().equals(histories.get(i+1).getContent())) {
                        userChangeCount.put(creator.getName(), userChangeCount.get(creator.getName()) + 1);
                    }
                }

                for (MondayComments comment : mondayCommentsRepository.findByMondayUpdate(update)) {
                    MondayUsers commentCreator = comment.getMondayCreatorUser();

                    List<MondayCommentHistory> commentHistories = mondayCommentHistoryRepository.findByMondayCommentId(comment.getId());

                    for (int i = 0; i<commentHistories.size()-1; i++) {
                        if (!commentHistories.get(i).getContent().equals(commentHistories.get(i+1).getContent())) {
                            userChangeCount.put(commentCreator.getName(), userChangeCount.get(commentCreator.getName()) + 1);
                        }
                    }
                }
            }

            usersNumberOfChanges.add(GetUserNumberOfChangesDto.builder()
                .boardName(mondayBoard.getName())
                .countOfChanges(userChangeCount.entrySet().stream()
                    .map(entry -> GetUserNumberOfChangesDto.CountOfChangesEachUser.builder()
                        .username(entry.getKey())
                        .totalChanges(entry.getValue())
                        .build())
                    .collect(Collectors.toList()))
                .build());
        }

        return usersNumberOfChanges;
    }

    public Long registerMondayConfiguration(String apiKey){
        MondayConfigurations mondayConfiguration = mondayConfigurationsRepository.save(MondayConfigurations.builder()
            .apiKey(apiKey)
            .build());

        syncUsers(mondayConfiguration.getId());
        try{
            syncBoardsWithItems(mondayConfiguration.getId());
        }catch (JsonProcessingException e) {
            log.error("syncBoardsWithItems error : {}", e.getMessage());
        }
        syncUpdatesAndComments(mondayConfiguration.getId());

        return mondayConfiguration.getId();
    }
}