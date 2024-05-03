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
import com.pbl.tasktoolintegration.monday.legacy.entity.MondayComment;
import com.pbl.tasktoolintegration.monday.legacy.entity.MondayItem;
import com.pbl.tasktoolintegration.monday.legacy.entity.MondayUpdate;
import com.pbl.tasktoolintegration.monday.legacy.entity.MondayUser;
import com.pbl.tasktoolintegration.monday.legacy.entity.MondayUserItem;
import com.pbl.tasktoolintegration.monday.legacy.model.GetAllBoardsWithColumnsMondayRes;
import com.pbl.tasktoolintegration.monday.legacy.model.GetAllItemsWithColumnMondayRes;
import com.pbl.tasktoolintegration.monday.legacy.model.GetAllUpdatesMondayRes;
import com.pbl.tasktoolintegration.monday.legacy.model.GetAllUsersMondayRes;
import com.pbl.tasktoolintegration.monday.legacy.model.GetStatusColumnIdInBoardWithSuccessIndexDto;
import com.pbl.tasktoolintegration.monday.legacy.model.GetUserAssignedItemsMondayRes;
import com.pbl.tasktoolintegration.monday.legacy.model.GetUserExpiredItemDto;
import com.pbl.tasktoolintegration.monday.legacy.model.GetUserResponseTimeRes;
import com.pbl.tasktoolintegration.monday.legacy.model.GetUsersAverageResponseTimeDto;
import com.pbl.tasktoolintegration.monday.model.MondayAssigneeInfo;
import com.pbl.tasktoolintegration.monday.legacy.model.MondayStatusColumnInfo;
import com.pbl.tasktoolintegration.monday.model.MondayStatusInfo;
import com.pbl.tasktoolintegration.monday.model.MondayTimelineColumnInfo;
import com.pbl.tasktoolintegration.monday.legacy.repository.MondayCommentRepository;
import com.pbl.tasktoolintegration.monday.legacy.repository.MondayItemRepository;
import com.pbl.tasktoolintegration.monday.legacy.repository.MondayUpdateRepository;
import com.pbl.tasktoolintegration.monday.legacy.repository.MondayUserItemRepository;
import com.pbl.tasktoolintegration.monday.legacy.repository.MondayUserRepository;
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
import java.util.Optional;
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
    private final MondayUserRepository mondayUserRepository;
    private final MondayUpdateRepository mondayUpdateRepository;
    private final MondayCommentRepository mondayCommentRepository;
    private final ObjectMapper objectMapper;
    private final MondayItemRepository mondayItemRepository;
    private final MondayUserItemRepository mondayUserItemRepository;

    // new
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

    // TODO: page 처리, 삭제 대응
    public GetAllUsersMondayRes getMondayUsers() {
        GraphQLRequest userRequest = GraphQLRequest.builder()
            .query(ModnayQuery.GET_ALL_USERS.getQuery())
            .build();
        return mondayWebClient.post()
            .bodyValue(userRequest.getRequestBody())
            .retrieve()
            .bodyToMono(GetAllUsersMondayRes.class)
            .block();
    }

    // TODO: page 처리, 삭제 대응
    public GetAllUpdatesMondayRes getMondayUpdates() {
        GraphQLRequest updateRequest = GraphQLRequest.builder()
            .query(ModnayQuery.GET_ALL_UPDATES.getQuery())
            .build();
        return mondayWebClient.post()
            .bodyValue(updateRequest.getRequestBody())
            .retrieve()
            .bodyToMono(GetAllUpdatesMondayRes.class)
            .block();
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

    public GetUserAssignedItemsMondayRes getUserAssignedItems(String boardId,
        String assignedColumnId, String assignedColumnValue, String timelineColumnId,
        String statusColumnId) {
        GraphQLRequest userAssignedItemsRequest = GraphQLRequest.builder()
            .query(String.format(ModnayQuery.GET_USER_ASSIGNED_ITEMS.getQuery(), boardId,
                assignedColumnId, assignedColumnValue, timelineColumnId, statusColumnId))
            .build();

        return mondayWebClient.post()
            .bodyValue(userAssignedItemsRequest.getRequestBody())
            .retrieve()
            .bodyToMono(GetUserAssignedItemsMondayRes.class)
            .onErrorReturn(GetUserAssignedItemsMondayRes.builder()
                .data(GetUserAssignedItemsMondayRes.Data.builder()
                    .items_page_by_column_values(
                        GetUserAssignedItemsMondayRes.ItemPageByColumnValues.builder()
                            .items(new ArrayList<>())
                            .build())
                    .build())
                .build())
            .block();
    }

    public GetAllBoardsWithColumnsMondayRes getAllBoardsWithColumns() {
        GraphQLRequest boardsRequest = GraphQLRequest.builder()
            .query(ModnayQuery.GET_ALL_BOARDS_WITH_COLUMNS.getQuery())
            .build();

        return mondayWebClient.post()
            .bodyValue(boardsRequest.getRequestBody())
            .retrieve()
            .bodyToMono(GetAllBoardsWithColumnsMondayRes.class)
            .block();
    }

    public GetAllItemsWithColumnMondayRes getAllItemsWithColumn(String boardId, String deadlineColumnId, String statusColumnId, String assigneeColumnId) {
        GraphQLRequest itemsRequest = GraphQLRequest.builder()
            .query(String.format(ModnayQuery.GET_ALL_ITEMS_WITH_COLUMN.getQuery(), boardId,
                deadlineColumnId, statusColumnId, assigneeColumnId))
            .build();

        return mondayWebClient.post()
            .bodyValue(itemsRequest.getRequestBody())
            .retrieve()
            .bodyToMono(GetAllItemsWithColumnMondayRes.class)
            .block();
    }

    private String getAssigneeColumnIdInBoard(GetAllBoardsWithColumnsMondayRes.Board board,
        String assigneeColumnName) {
        for (GetAllBoardsWithColumnsMondayRes.Column column : board.getColumns()) {
            if (column.getTitle().equals(assigneeColumnName)) {
                return column.getId();
            }
        }
        return "";
    }

    private String getDeadlineColumnIdInBoard(GetAllBoardsWithColumnsMondayRes.Board board,
        String deadlineColumnName) {
        for (GetAllBoardsWithColumnsMondayRes.Column column : board.getColumns()) {
            if (column.getTitle().equals(deadlineColumnName)) {
                return column.getId();
            }
        }
        return "";
    }

    private GetStatusColumnIdInBoardWithSuccessIndexDto getStatusColumnIdInBoardWithSuccessIndex(
        GetAllBoardsWithColumnsMondayRes.Board board, String statusColumnName,
        String completeStatusName) {
        GetStatusColumnIdInBoardWithSuccessIndexDto statusColumnIdWithSuccessIndex = GetStatusColumnIdInBoardWithSuccessIndexDto.builder()
            .statusColumnId("")
            .completeStatusIndex(0)
            .build();
        for (GetAllBoardsWithColumnsMondayRes.Column column : board.getColumns()) {
            if (column.getTitle().equals(statusColumnName)) {
                statusColumnIdWithSuccessIndex.setStatusColumnId(column.getId());
                MondayStatusInfo statusInfo = null;
                try {
                    statusInfo = objectMapper.readValue(column.getSettings_str(),
                        MondayStatusInfo.class);
                } catch (JsonProcessingException e) {
                    statusInfo.setLabels(new HashMap<>());
                }
                for (String key : statusInfo.getLabels().keySet()) {
                    if (statusInfo.getLabels().get(key).equals(completeStatusName)) {
                        statusColumnIdWithSuccessIndex.setCompleteStatusIndex(
                            Integer.parseInt(key));
                        break;
                    }
                }
            }
        }
        return statusColumnIdWithSuccessIndex;
    }

    private boolean isExpiredItem(GetUserAssignedItemsMondayRes.Item item, String deadlineColumnId,
        String statusColumnId, Integer completeStatusIndex) throws JsonProcessingException {
        Date deadline = null;
        boolean isComplete = true;

        for (GetUserAssignedItemsMondayRes.ColumnValue columnValue : item.getColumn_values()) {
            // 데드라인 값 조회
            if (columnValue.getId().equals(deadlineColumnId)) {
                deadline = objectMapper.readValue(columnValue.getValue(),
                    MondayTimelineColumnInfo.class).getTo();
            }
            // 상태 값 조회
            if (columnValue.getId().equals(statusColumnId)) {
                Integer statusIndex = objectMapper.readValue(columnValue.getValue(),
                    MondayStatusColumnInfo.class).getIndex();
                if (statusIndex != completeStatusIndex) {
                    isComplete = false;
                }
            }
        }
        // 완료되지 않았으며 데드라인이 현재 일자 기준으로 지났다면 만료된 아이템으로 판단
        if (isComplete == false && deadline != null && deadline.before(new Date())) {
            return true;
        }
        return false;
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

    public List<GetUserExpiredItemDto> getUsersExpiredItemOld() throws JsonProcessingException {
        GetAllUsersMondayRes mondayUsers = getMondayUsers();
        // 보드와 해당 보드에 존재하는 컬럼에 대한 정보 조회
        GetAllBoardsWithColumnsMondayRes mondayBoards = getAllBoardsWithColumns();
        // 유저별 만료된 아이템 개수 저장
        Map<String, Integer> userExpiredItemCount = new HashMap<>();

        // 동명이인 케이스 제외하기 때문에 이름을 키값으로 설정
        for (GetAllUsersMondayRes.User user : mondayUsers.getData().getUsers()) {
            userExpiredItemCount.put(user.getName(), 0);
        }

        // 보드마다 순회하며 계산
        for (GetAllBoardsWithColumnsMondayRes.Board board : mondayBoards.getData().getBoards()) {
            // 담당자 컬럼, 데드라인 컬럼, 상태 컬럼 id 값 찾기
            String assigneeColumnId = getAssigneeColumnIdInBoard(board, "담당자");
            String deadlineColumnId = getDeadlineColumnIdInBoard(board, "데드라인");
            // 상태 컬럼 id 값, 완료 상태 index 찾기
            GetStatusColumnIdInBoardWithSuccessIndexDto statusColumnInfo = getStatusColumnIdInBoardWithSuccessIndex(
                board, "상태", "완료");
            String statusColumnId = statusColumnInfo.getStatusColumnId();
            Integer completeStatusIndex = statusColumnInfo.getCompleteStatusIndex();

            // 유저별로 해당 보드에 만료된 아이템 개수 계산
            for (String username : userExpiredItemCount.keySet()) {
                GetUserAssignedItemsMondayRes mondayItems = getUserAssignedItems(board.getId(),
                    assigneeColumnId, username, deadlineColumnId, statusColumnId);

                // 아이템 순회하며 만료 여부 확인
                for (GetUserAssignedItemsMondayRes.Item item : mondayItems.getData()
                    .getItems_page_by_column_values().getItems()) {
                    if (isExpiredItem(item, deadlineColumnId, statusColumnId, completeStatusIndex)) {
                        userExpiredItemCount.put(username, userExpiredItemCount.get(username) + 1);
                    }
                }
            }
        }

        List<GetUserExpiredItemDto> userExpiredItems = new ArrayList<>();
        for (String username : userExpiredItemCount.keySet()) {
            userExpiredItems.add(GetUserExpiredItemDto.builder()
                .username(username)
                .totalExpiredItems(userExpiredItemCount.get(username))
                .build());
        }
        return userExpiredItems;
    }

    // TODO: 수정 동기화 처리
    public void syncItems() throws JsonProcessingException {
        GetAllBoardsWithColumnsMondayRes mondayBoards = getAllBoardsWithColumns();
        for (GetAllBoardsWithColumnsMondayRes.Board board : mondayBoards.getData().getBoards()) {
            String assigneeColumnId = getAssigneeColumnIdInBoard(board, "담당자");
            String deadlineColumnId = getDeadlineColumnIdInBoard(board, "데드라인");
            GetStatusColumnIdInBoardWithSuccessIndexDto statusColumnInfo = getStatusColumnIdInBoardWithSuccessIndex(
                board, "상태", "완료");
            String statusColumnId = statusColumnInfo.getStatusColumnId();
            Integer completeStatusIndex = statusColumnInfo.getCompleteStatusIndex();

            GetAllItemsWithColumnMondayRes items = getAllItemsWithColumn(board.getId(), deadlineColumnId, statusColumnId, assigneeColumnId);

            for (GetAllItemsWithColumnMondayRes.Item item : items.getData().getBoards().get(0).getItems_page().getItems()) {
                Date deadLine = null;
                Boolean isComplete = null;
                List<MondayUser> assignee = new ArrayList<>();
                for (GetAllItemsWithColumnMondayRes.ColumnValue columnValue : item.getColumn_values()) {
                    if (columnValue.getId().equals(deadlineColumnId) && columnValue.getValue() != null){
                        deadLine = objectMapper.readValue(columnValue.getValue(), MondayTimelineColumnInfo.class).getTo();
                    }
                    if (columnValue.getId().equals(statusColumnId) && columnValue.getValue() != null){
                        Integer statusIndex = objectMapper.readValue(columnValue.getValue(), MondayStatusColumnInfo.class).getIndex();
                        if (statusIndex != completeStatusIndex) {
                            isComplete = false;
                        }else {
                            isComplete = true;
                        }
                    }

                    if (columnValue.getId().equals(assigneeColumnId) && columnValue.getValue() != null) {
                        MondayAssigneeInfo assigneeId = objectMapper.readValue(
                            columnValue.getValue(), MondayAssigneeInfo.class);
                        for (MondayAssigneeInfo.Person person : assigneeId.getPersonsAndTeams()) {
                            assignee.add(
                                mondayUserRepository.findById(person.getId().toString()).get());
                        }
                    }
                }

                MondayItem savedItem = mondayItemRepository.save(MondayItem.builder()
                    .id(item.getId())
                    .deadLine(deadLine)
                    .isComplete(isComplete)
                    .build());

                for (MondayUser user : assignee) {
                    mondayUserItemRepository.save(MondayUserItem.builder()
                        .mondayItem(savedItem)
                        .mondayUser(user)
                        .build());
                }
            }
        }
    }

    public List<GetUserExpiredItemDto> getBatchUsersExpiredItem() throws JsonProcessingException {
        List<MondayUser> mondayUsers = mondayUserRepository.findAll();

        Map<String, Integer> userExpiredItemCount = new HashMap<>();
        for (MondayUser user : mondayUsers) {
            userExpiredItemCount.put(user.getName(), 0);
        }

        List<MondayItem> mondayItems = mondayItemRepository.findAll();

        for (MondayItem item : mondayItems) {
            Date deadline = item.getDeadLine();
            Boolean isComplete = item.getIsComplete();
            if (isComplete != null && isComplete == false && deadline != null && deadline.before(
                new Date())) {
                List<MondayUserItem> userItems = mondayUserItemRepository.findByMondayItem(item);
                for (MondayUserItem userItem : userItems) {
                    userExpiredItemCount.put(userItem.getMondayUser().getName(),
                        userExpiredItemCount.get(userItem.getMondayUser().getName()) + 1);
                }
            }
        }

        List<GetUserExpiredItemDto> userExpiredItems = new ArrayList<>();
        for (String username : userExpiredItemCount.keySet()) {
            userExpiredItems.add(GetUserExpiredItemDto.builder()
                .username(username)
                .totalExpiredItems(userExpiredItemCount.get(username))
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