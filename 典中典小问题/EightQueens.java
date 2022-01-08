public class EightQueens {
    int n=8;
    int cnt=0;
    int [][]map=new int[n][n];
    void print(){
        for(int i=0;i<n;++i){
            for(int j=0;j<n;++j){
                System.out.print(map[i][j]+" ");
            }
            System.out.println();
        }
    }
    boolean isValid(int x,int y){
        for(int i=0;i<n;++i){
            if(map[i][y]==1){//同列有子
                return false;
            }
        }
        for(int i=x,j=y;i>=0&&j>=0;--i,--j){
            if(map[i][j]==1){//左上角有子
                return false;
            }
        }
        for(int i=x,j=y;i>=0&&j<n;--i,++j){
            if(map[i][j]==1){//左下角有子
                return false;
            }
        }
        return true;
    }
    void entry(int x){
        if(x>7){
            cnt++;
            System.out.println(cnt);
            print();
            return;
        }
        for(int y=0;y<8;++y){//从左到右试
            if(isValid(x,y)){
                map[x][y]=1;
                entry(x+1);//处理下一行
                map[x][y]=0;
            }
        }
    }
    public static void main(String[] args) {
        EightQueens eightQueens=new EightQueens();
        eightQueens.entry(0);
    }
}
