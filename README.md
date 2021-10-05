# Tetris
해당 링크에서 참고하여 제작   
[rlaquddn05/tetris]: https://github.com/rlaquddn05/tetris   
[how-to-write-by-markdown.md]: https://gist.github.com/ihoneymon/652be052a0727ad59601   
[Google](http://www.google.co.kr)   

## 1. 프로젝트 소개   
본 프로젝트에서는 고전 퍼즐 게임 테트리스를 이클립스를 활용하여 자바로 구현해보려고 한다.

이번 프로젝트는 SOLO mode와 소켓 통신을 활용한 두명의 사용자가 대전을 할 수 있도록 구현하는 것을   
목표로 제작되었고, 추후 사용자 등록기능, 동시접속자 수용 등을 추가할 수 있도록 고려하고 작업하였다.

## 2. 사용된 프로그램
>* JDK 1.8   
>* Eclipse

## 3. 게임에 대한 설명, 세부 규칙
### 테트리스란?
테트리스는 블록을 놓으며 쌓여가는 블록들이 맨 위까지 쌓이지 않게 줄을 꽉 채워   
한 줄, 한 줄 지우며 버티면서 플레이 하는 퍼즐 게임이다.   

### 테트로미노

기본 블록은 4개의 정사각형 벽돌로 조합되어있다.   
4개의 벽돌을 이어붙여서 만든 모양으로 7가지가 있다.   
이 블록들을 '테트로미노'라고 부른다.   

> TTC 세계 표준 테트로미노 색   
   
I형 : 하늘
- 최대 4줄격파가 가능.   
 
O형 : 노랑
- 네모난 블록특성상 유일하게 방향전환이 드러나지않지만, 게임 내부적으로는 방향전환이 되는 것으로 취급되기 때문에   
  인피니티 룰이 적용될 경우 O미노를 회전시키고 있을 경우 최종 고정이 되지않는다.   
  
Z형 : 빨강, S형 : 연두
- 좌측의 빨간 블록이 Z, 우측의 초록 블록이 S로 대칭형을 구분해 부른다.   

J형 : 파랑, L형 : 주황
- 긴쪽을 세운 것을 기준으로 좌측의 파란색 J와 우측의 주황색 L로 구분.   

T형 : 보라
- T스핀이 가능한 T자형 블록.
- 특히 배틀 모드가 있는 테트리스에서는 아예 제 2의 I미노라고 할 수 있을 정도로 그 중요성이 크다.  

#### 미노의 등장과 규칙
미노의 등장(가로 10줄)   
O미노와 I미노(i미노)는 가운데에, 나머지(TLJSZ)는 왼쪽에서 세 칸 떨어져서 나옵니다.   

미노가 나오는 규칙
7종의 미노가 한 묶음으로 나온다. (7-bag)   
   
n번째 나오는 미노를 n번 미노라 하자.   
그러면 1~7번 미노는 I, O, T, L, J, S, Z가 무작위로 나온다.
그리고 8~14번 미노는 또 I, O, T, L, J, S, Z가 무작위로 나온다.   
그리고 15~21번 미노는 또 I, O, T, L, J, S, Z가 무작위로 나온다...   
이후에도 같은 식으로 반복   


## 4. 프로그램의 장점 및 특징
## 5. 부족한, 힘들었던 부분 -> 해결한 부분
## 6. 개선해야할 부분
## 7. 시연
