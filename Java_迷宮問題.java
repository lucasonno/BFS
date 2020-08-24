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