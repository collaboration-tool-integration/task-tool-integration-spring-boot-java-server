package com.pbl.tasktoolintegration.monday;

import com.pbl.tasktoolintegration.monday.entity.MondayComment;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdate;
import com.pbl.tasktoolintegration.monday.entity.MondayUser;
import com.pbl.tasktoolintegration.monday.model.GetAllUpdatesMondayRes;
import com.pbl.tasktoolintegration.monday.model.GetAllUsersMondayRes;
import com.pbl.tasktoolintegration.monday.model.GetUsersAverageResponseTimeDto;
import com.pbl.tasktoolintegration.monday.repository.MondayCommentRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayUpdateRepository;
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

    private List<String> getMentionedUsersInString(String text) {
        List<String> mentionedUsers = new ArrayList<>();
        String[] words = text.split(" ");
        for (String word : words) {
            if (word.length() >= 1 && word.charAt(0) == '@' && mentionedUsers.contains(word.substring(1)) == false) {
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
                Optional<MondayComment> mondayComment = mondayCommentRepository.findById(reply.getId());
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
}
