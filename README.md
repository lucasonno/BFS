# Java自學紀錄 - 迷宮問題 (BFS)
## 廣度優先搜索 (Breadth First Search) 
>走訪的順序為將某個頂點視為起點，接著拜訪和起點相鄰的所有頂點，再走訪與起點相鄰的頂點的相鄰頂點，也就是在走訪更外一層的意思，持續更外層找相鄰頂點，直到所有連通頂點都被走訪過了。然而BFS中最經典的問題就是迷宮問題。

## 迷宮問題
我們先以一個二為陣列 maze[1...m][1...p] 表示迷宮,其中```1```代表不能通行```0```代表可以通過,出口為 maze[m][p]。
>如下圖

![](https://i.imgur.com/US5s90O.png)
**執行結果**
* 輸出迷宮的正確路徑

而我們所在位置可用 [i,j] 表第 i 列第 j 行,我們可用X來表示,如果X周圍都是```0```的話,我們可以在這4條路任選一條作為下次移動的方向,分別為```西``` ```南``` ```東``` ```北``` ,用代號分別為```W``` ```S``` ```E``` ```N```。並且各給予一個值 ```dire```。

|  方向  | dire | x座標向量 | y座標向量 |
|:------:|:----:|:---------:|:---------:|
| W (西) |  1   |     0     |     1     |
| S (南) |  2   |     1     |     0     |
| E (東) |  3   |     0     |    -1     |
| N (北) |  4   |    -1     |     0     |

以上四個方向可用一個二維陣列 move[][] 來儲存所有可能的方向
```java=
int move[][] = {{0,1},{1,0},{0,-1},{-1,0}};
```
如果我們位於迷宮 [i,j] 位置,同時想找在我們南方位置的 [g,h] ,dire = 2,那麼我們就設 : 
```java=
g = i + move[dire][1]; //dire = 2
h = i + move[dire][2]; //dire = 2
```
我們還必須注意,並非每個位置都有4個相鄰點可以移動,若 [i,j] 在邊界上,即 i=1或m , j=1或p ,則只有三個相鄰點,而不是4個。為了避免這種情況發生,可以假設迷宮最外圍都包著 ```1``` ,因此 maze陣列要宣告[0...m+1][0...p+1]。

我們在移動時先從```W(西)```開始嘗試,此路不同則回到原先位置再依逆時鐘方向往其他```dire```嘗試。最後為了防止走重複的路,我們另外用一個二為陣列 mark[0...m+1][0...p+1] ,這個陣列初始值都是0,於當我們走到某一點 [i,j] 時, mark[i,j]就改為1。要記得把迷宮終點設為 maze[m][p]=0 ,否則沒有路可以通到出口。

**以下為迷宮演繹邏輯**
1. 只要堆疊不是空的,表示還有路可以走
```java=
while(t != 0)
```
2. 每一個位置均有4個方向可以嘗試
```java=
while(dire <= 4)
```
3. 計算下一步的座標
```java=
g = i + move[dire][1]; 
h = i + move[dire][2];
```
4. 假如(g,h)為終點座標,則探索成功。此時mark的內容即為我們走過的路徑
```java=
if(g==m & h==p){
    mark[i][j] = 1;
    mark[g][h] = 1;
    System.out.println("迷宮走過路徑 : ");
    System.out.println("(1代表走過)");
    PrintMark(); //印出走過路徑
    return ; //結束程式
}
```
5. 假如(g,h)為通路,且還沒走過,則將此點做記號,將目前座標放入堆疊(記錄此點的座標和方向),且設 dire=1 ,否則繼續嘗試下一個方向。
```java=
if(maze[g][h]==0 & mark[g][h]==0){
    push(i,j,dire); //存取當前走過方向與座標
    //重設當前位置
    i = g;
    j = h;
    dire = 1; //走完後,將方向設為初始值,繼續探索
}
```
6. 假如4個方向都已經試過,且全無路徑可以移動,則應自堆疊中拿取一個位置,重新嘗試其他可能路徑。
```java=
pop();
```
## 迷宮實做
```java=
public class Java_迷宮問題 {
    static int move[][] = new int[5][3]; //移動方向
    static int maze[][] = new int[10][10]; //10x10迷宮
    static int mark[][] = new int[10][10]; //做記號
    static int s[][] = new int[101][4]; //堆疊,存取走過路徑與方向
    static int g,h; //探測位置
    static int m,p; //終點座標
    static int i,j; //起始位置
    static int dire; //方向
    static int t; //堆疊指標
    public static void main(String[] args) {
        //設定迷宮陣列
        String s = String.valueOf(0);
        String mm[] = new String[10]; //13行
        String ps = ""; //用字串紀錄迷宮圖
         mm[0] = "1111111111";
         mm[1] = "1000000011";
         mm[2] = "1111111011";
         mm[3] = "1011000011";
         mm[4] = "1100011101";
         mm[5] = "1101111101";
         mm[6] = "1100000111";
         mm[7] = "1011110001";
         mm[8] = "1001101101";
         mm[9] = "1111111111";
        //讀取迷宮
        for(int i=0;i<=9;i++){ //10行
            for(int j=0;j<=9;j++){ //10列
                s = mm[i].substring(j,j+1); //擷取mm[i]從j之後到j+1的字串,為了maze,mark存取
                ps += s + " ";
                maze[i][j] = Integer.parseInt(s); //將s轉為Int型態,並存入迷宮
                mark[i][j] = 0; //表尚未走過
            }
            ps += '\n'; //每行完成就換行
        }
        //輸出迷宮
        System.out.println("迷宮圖 : ");
        System.out.println("(0代表可以走)");
        System.out.println(ps);
        //設定方向,idk
        move[1][1] = 0; move[1][2] = 1; //left
        move[2][1] = 1; move[2][2] = 0; //up
        move[3][1] = 0; move[3][2] = -1; //right
        move[4][1] = -1; move[4][2] = 0; //down
        i = 1; j = 1; //起始座標
        m = 8; p = 8; //終點座標
        t = 1; //堆疊指標
        dire = 1; //方向由左方開始,並順時鐘輪轉
        while(t != 0){ //當t=0時表無法走出此迷宮
            while(dire <= 4){ //嘗試4個方向
                g = i + move[dire][1];
                h = j + move[dire][2];
                //當到達終點時,停止迷宮並印出mark
                if(g==m & h==p){
                    mark[i][j] = 1;
                    mark[g][h] = 1;
                    System.out.println("迷宮走過路徑 : ");
                    System.out.println("(1代表走過)");
                    PrintMark();
                    return ; //結束程式
                }
                //若此方向可走且尚未走過
                if(maze[g][h]==0 & mark[g][h]==0){
                    push(i,j,dire); //存取當前走過方向與座標
                    //重設當前位置
                    i = g;
                    j = h;
                    dire = 1; //走完後,將方向設為初始值,繼續探索
                }
                //此路不通時,換方向
                else{
                    dire++;
                }
            }
            pop();
        }
      //System.out.println("無法走出此迷宮");
    }
    static void PrintMark(){ //輸出走過路徑
        String s="";
        for(int k=1;k<=8;k++){ //走的路徑為第1~8行
            for(int l=1;l<=8;l++){ //走的路徑為第1~8列
                s = s + " " + String.valueOf(mark[k][l]);
            }
            s += '\n';
        }
        System.out.println(s);
    }
    //將走過的方向與座標放入堆疊
    static void push(int i,int j,int dire){
        mark[i][j] = 1;
        s[t][1] = i;
        s[t][2] = j;
        s[t][3] = dire;
        t++;
    }
    //4個方向都不通,從堆疊中取前一個位置,重新嘗試可能的路徑
    static void pop(){
        t--; //找上一次的紀錄
        i = s[t][1];
        j = s[t][2];
        dire = s[t][3];
        mark[i][j] = 0; //刪除錯誤路徑
    }
}
```
>下圖為執行結果

![](https://i.imgur.com/16Mk0xg.png)
