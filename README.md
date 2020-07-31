## coucle-user
스프링 부트로 만든 유저 서비스 앱. 
(Async, spring-cloud-feign, spring-cloud-eureka 적용 중)

## Database
MYSQL (AWS RDS 연동)

## Authentication & Authorization
Spring security Filter 적용
JWT 기반 인증 & 인가 구현

## kafka producer
유저 가입 완료시 `email.notification` topic으로 메일 전송 요청 payload를 담아 publish
위 토픽을 구독하는 notification 서비스에서는 이벤트를 받아서 메일 전송 처리.
(https://github.com/JisooJang/coucle-notification/blob/master/src/main/java/com/example/notification/listener/AlarmTalkListener.java#L72)