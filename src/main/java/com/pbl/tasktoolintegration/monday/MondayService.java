package com.pbl.tasktoolintegration.monday;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.tasktoolintegration.monday.entity.MondayComment;
import com.pbl.tasktoolintegration.monday.entity.MondayItem;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdate;
import com.pbl.tasktoolintegration.monday.entity.MondayUser;
import com.pbl.tasktoolintegration.monday.entity.MondayUserItem;
import com.pbl.tasktoolintegration.monday.model.GetAllBoardsWithColumnsMondayRes;
import com.pbl.tasktoolintegration.monday.model.GetAllItemsWithColumnMondayRes;
import com.pbl.tasktoolintegration.monday.model.GetAllUpdatesMondayRes;
import com.pbl.tasktoolintegration.monday.model.GetAllUsersMondayRes;
import com.pbl.tasktoolintegration.monday.model.GetStatusColumnIdInBoardWithSuccessIndexDto;
import com.pbl.tasktoolintegration.monday.model.GetUserAssignedItemsMondayRes;
import com.pbl.tasktoolintegration.monday.model.GetUserExpiredItemDto;
import com.pbl.tasktoolintegration.monday.model.GetUsersAverageResponseTimeDto;
import com.pbl.tasktoolintegration.monday.model.MondayAssigneeInfo;
import com.pbl.tasktoolintegration.monday.model.MondayStatusColumnInfo;
import com.pbl.tasktoolintegration.monday.model.MondayStatusInfo;
import com.pbl.tasktoolintegration.monday.model.MondayTimelineColumnInfo;
import com.pbl.tasktoolintegration.monday.repository.MondayCommentRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayItemRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayUpdateRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayUserItemRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayUserRepository;
import graphql.kickstart.spring.webclient.boot.GraphQLRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    private List<String> getMentionedUsersInString(String text) {
        List<String> mentionedUsers = new ArrayList<>();
        String[] words = text.split(" ");
        for (String word : words) {
            if (word.length() >= 1 && word.charAt(0) == '@'
                && mentionedUsers.contains(word.substring(1)) == false) {
                mentionedUsers.add(word.substring(1));
            }
        }
        return mentionedUsers;
    }

    private int calculateResponseTime(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return (int) (diff / 1000);
    }

    public void syncUsers() {
        GetAllUsersMondayRes mondayUsers = getMondayUsers();

        for (GetAllUsersMondayRes.User user : mondayUsers.getData().getUsers()) {
            Optional<MondayUser> mondayUser = mondayUserRepository.findById(user.getId());
            if (mondayUser.isPresent()) {
                mondayUser.get().updateName(user.getName());
                mondayUserRepository.save(mondayUser.get());
            } else {
                mondayUserRepository.save(MondayUser.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .build());
            }
        }
    }

    public void syncUpdates() {
        GetAllUpdatesMondayRes mondayUpdates = getMondayUpdates();

        for (GetAllUpdatesMondayRes.Update update : mondayUpdates.getData().getUpdates()) {
            Optional<MondayUpdate> mondayUpdate = mondayUpdateRepository.findById(update.getId());
            if (mondayUpdate.isPresent()) {
                if (mondayUpdate.get().getUpdatedAt().before(update.getUpdated_at())) {
                    mondayUpdate.get().updateContent(update.getText_body());
                    mondayUpdate.get().updateUpdatedAt(update.getUpdated_at());
                    mondayUpdateRepository.save(mondayUpdate.get());
                }
            } else {
                mondayUpdateRepository.save(MondayUpdate.builder()
                    .id(update.getId())
                    .content(update.getText_body())
                    .createdAt(update.getCreated_at())
                    .updatedAt(update.getUpdated_at())
                    .creator(mondayUserRepository.findById(update.getCreator().getId()).get())
                    .build());
            }

            // 댓글 업데이트
            for (GetAllUpdatesMondayRes.Reply reply : update.getReplies()) {
                Optional<MondayComment> mondayComment = mondayCommentRepository.findById(
                    reply.getId());
                if (!mondayComment.isPresent()) {
                    mondayCommentRepository.save(MondayComment.builder()
                        .id(reply.getId())
                        .createdAt(reply.getCreated_at())
                        .creator(mondayUserRepository.findById(reply.getCreator().getId()).get())
                        .update(mondayUpdateRepository.findById(update.getId()).get())
                        .build());
                }
            }
        }
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

    public List<GetUsersAverageResponseTimeDto> getUsersAverageResponseTime() {
        // 전체 유저 조회
        GetAllUsersMondayRes mondayUsers = getMondayUsers();

        // 유저 이름과 1차 응답 시간을 저장할 맵 생성, 초기화
        Map<String, String> usernameMap = new HashMap<>();
        Map<String, List<Integer>> userResponseTimeMap = new HashMap<>();
        for (GetAllUsersMondayRes.User user : mondayUsers.getData().getUsers()) {
            usernameMap.put(user.getId(), user.getName());
            userResponseTimeMap.put(user.getId(), new ArrayList<>());
        }

        // 모든 업데이트 조회
        GetAllUpdatesMondayRes mondayUpdates = getMondayUpdates();

        for (GetAllUpdatesMondayRes.Update update : mondayUpdates.getData().getUpdates()) {
            // update에 언급된 유저 찾기
            List<String> mentionedUsers = getMentionedUsersInString(update.getText_body());

            // update 내용에 유저 언급이 있을 경우
            if (mentionedUsers.size() > 0) {
                // 언급 시간
                Date startDate = update.getCreated_at();
                // 최초 1회 응답이기 때문에 모든 언급 유저를 leftUsers에 저장 후 답장한 경우 pop
                List<String> leftUsers = new ArrayList<>(mentionedUsers);
                // 답장했는지 순회하며 탐색
                for (GetAllUpdatesMondayRes.Reply reply : update.getReplies()) {
                    if (mentionedUsers.contains(reply.getCreator().getName())) {
                        // 답장 시간 계산
                        int diffSeconds = calculateResponseTime(startDate, reply.getCreated_at());

                        userResponseTimeMap.get(reply.getCreator().getId()).add(diffSeconds);
                        leftUsers.remove(reply.getCreator().getName());
                    }
                }
            }
        }

        List<GetUsersAverageResponseTimeDto> usersAverageResponseTime = new ArrayList<>();
        for (String userId : userResponseTimeMap.keySet()) {
            List<Integer> responseTimes = userResponseTimeMap.get(userId);
            // 응답한 기록이 없는 경우 분석 불가
            if (responseTimes.size() == 0) {
                continue;
            }

            int sum = 0;
            for (int responseTime : responseTimes) {
                sum += responseTime;
            }
            int average = sum / responseTimes.size();
            usersAverageResponseTime.add(GetUsersAverageResponseTimeDto.builder()
                .username(usernameMap.get(userId))
                .averageResponseTime(average)
                .build());
        }

        return usersAverageResponseTime;
    }

    public List<GetUsersAverageResponseTimeDto> getBatchUsersAverageResponseTime() {
        List<MondayUser> mondayUsers = mondayUserRepository.findAll();

        Map<String, List<Integer>> userResponseTimeMap = new HashMap<>();
        for (MondayUser user : mondayUsers) {
            userResponseTimeMap.put(user.getId(), new ArrayList<>());
        }

        List<MondayUpdate> mondayUpdates = mondayUpdateRepository.findAll();

        for (MondayUpdate update : mondayUpdates) {
            List<String> mentionedUsers = getMentionedUsersInString(update.getContent());
            if (mentionedUsers.size() > 0) {
                Date startDate = update.getCreatedAt();
                List<String> leftUsers = new ArrayList<>(mentionedUsers);
                List<MondayComment> mondayComments = mondayCommentRepository.findByUpdate(update);
                for (MondayComment comment : mondayComments) {
                    if (mentionedUsers.contains(comment.getCreator().getName())) {
                        int diffSeconds = calculateResponseTime(startDate, comment.getCreatedAt());
                        userResponseTimeMap.get(comment.getCreator().getId()).add(diffSeconds);
                        leftUsers.remove(comment.getCreator().getName());
                    }
                }
            }
        }

        List<GetUsersAverageResponseTimeDto> usersAverageResponseTime = new ArrayList<>();
        for (String userId : userResponseTimeMap.keySet()) {
            List<Integer> responseTimes = userResponseTimeMap.get(userId);
            // 응답한 기록이 없는 경우 분석 불가
            if (responseTimes.size() == 0) {
                continue;
            }

            int sum = 0;
            for (int responseTime : responseTimes) {
                sum += responseTime;
            }
            int average = sum / responseTimes.size();
            usersAverageResponseTime.add(GetUsersAverageResponseTimeDto.builder()
                .username(mondayUserRepository.findById(userId).get().getName())
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

    public List<GetUserExpiredItemDto> getUsersExpiredItem() throws JsonProcessingException {
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

                MondayItem savedItem = mondayItemRepository.save(com.pbl.tasktoolintegration.monday.entity.MondayItem.builder()
                    .id(item.getId())
                    .deadLine(deadLine)
                    .isComplete(isComplete)
                    .build());

                for (MondayUser user : assignee) {
                    if (!mondayUserItemRepository.existsByMondayItemAndMondayUser(savedItem, user)){
                        mondayUserItemRepository.save(MondayUserItem.builder()
                            .mondayItem(savedItem)
                            .mondayUser(user)
                            .build());
                    }
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
            if (isComplete != null && isComplete == false && deadline != null && deadline.before(new Date())) {
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
}