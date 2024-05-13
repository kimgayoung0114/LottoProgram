# 가상 로또 프로그램

- 학습 목표 :
  1. Java Swing을 이용한 GUI에 대한 이해
  2. 배열과 컬렉션을 포함한 자료구조에 대한 이해
  3. 이벤트 처리 방법에 대한 이해

- 기능 설명 :
  1. 구매 페이지 :수동, 반자동, 자동 버튼을 클릭해 원하는 번호 선택 하여 구매  
  2. 구매 내역 페이지 : 구매 내역 저장
  3. 결과 창 페이지 : 당첨 번호와 비교하여 결과 확인
  
- 문제 사항 및 해결 :
   사용자의 버튼 선택의 예외 상황(중복선택, 개수 초과 선택 등)으로 인한 오류
    해결 : 선택이 불필요한 경우 버튼 비활성화 등의 사용자의 상호 작용을 제한하여 예외 상황 방지
   당첨 결과와 등수 표현 금액계산 오류
    원인 : 당첨 결과를 비교하는 하나의 메소드에서 등수와 금액계산을 모두 하기에 로직이 복잡해 오류 발생 시 원인 분석이 어려움
    해결 : 로직의 복잡성을 줄이기 위해 기능별 메소드 세분화
   팀원들과 코드 병합 시 발생 문제
    원인 : 코드 병합 시 팀원들 사이의 소통이 부족하여, 각자가 작성한 코드의 충돌이나 이해를 돕기 위한 설명 부재
    해결 : 팀원들과 많은 소통을 강화 역할 분담을 명확히 하고, 코드 설명과 피드백을 하여 문제해결
