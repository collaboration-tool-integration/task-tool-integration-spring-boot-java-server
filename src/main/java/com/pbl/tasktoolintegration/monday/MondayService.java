package com.pbl.tasktoolintegration.monday;

import com.pbl.tasktoolintegration.monday.model.GetAllUpdatesMondayRes;
import com.pbl.tasktoolintegration.monday.model.GetAllUsersMondayRes;
import graphql.kickstart.spring.webclient.boot.GraphQLRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class MondayService {
    private final WebClient mondayWebClient;

    // TODO: 함수 분리 필요
    public void queryTest() {
        // update 언급 -> reply 시간 계산

        // 전체 유저 조회
        GraphQLRequest userRequest = GraphQLRequest.builder()
            .query(ModnayQuery.GET_ALL_USERS.getQuery())
            .build();
        GetAllUsersMondayRes response = mondayWebClient.post()
            .bodyValue(userRequest.getRequestBody())
            .retrieve()
            .bodyToMono(GetAllUsersMondayRes.class)
            .block();

        // 유저 이름과 1차 응답 시간을 저장할 맵 생성, 초기화
        Map<String, String> usernameMap = new HashMap<>();
        Map<String, List<Integer>> userResponseTimeMap = new HashMap<>();
        for (GetAllUsersMondayRes.User user : response.getData().getUsers()) {
            usernameMap.put(user.getId(), user.getName());
            userResponseTimeMap.put(user.getId(), new ArrayList<>());
        }

        // 모든 업데이트 조회
        GraphQLRequest updateRequest = GraphQLRequest.builder()
            .query(ModnayQuery.GET_ALL_UPDATES.getQuery())
            .build();
        GetAllUpdatesMondayRes updateResponse = mondayWebClient.post()
            .bodyValue(updateRequest.getRequestBody())
            .retrieve()
            .bodyToMono(GetAllUpdatesMondayRes.class)
            .block();

        for (GetAllUpdatesMondayRes.Update update : updateResponse.getData().getUpdates()) {
            // update에 언급된 유저 찾기
            List<String> mentionedUsers = new ArrayList<>();
            String[] words = update.getText_body().split(" ");
            for (String word : words) {
                if (word.length() >= 1 && word.charAt(0) == '@' && mentionedUsers.contains(word.substring(1)) == false) {
                    mentionedUsers.add(word.substring(1));
                }
            }

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
                        Date endDate = reply.getCreated_at();
                        long diff = endDate.getTime() - startDate.getTime();
                        int diffSeconds = (int) (diff / 1000);

                        userResponseTimeMap.get(reply.getCreator().getId()).add(diffSeconds);
                        leftUsers.remove(reply.getCreator().getName());
                    }
                }
            }
        }
        // 유저별 평균 응답 시간 출력
        for (String userId : userResponseTimeMap.keySet()) {
            List<Integer> responseTimes = userResponseTimeMap.get(userId);
            // 응답한 기록이 없는 경우 분석 불가
            if (responseTimes.size() == 0) {
                log.info("User: " + usernameMap.get(userId) + ", Average Response Time: " + "NOTFOUND");
                continue;
            }

            int sum = 0;
            for (int responseTime : responseTimes) {
                sum += responseTime;
            }
            int average = sum / responseTimes.size();
            log.info("User: " + usernameMap.get(userId) + ", Average Response Time: " + average + " seconds");
        }
    }
}
