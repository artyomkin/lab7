package clientPackage;

public class MainClient {
    public static void main(String[] args){
       /* for(int i = 0; i<1; i++){
            new Thread(new Client("127.0.0.1",2232)).start();
        }*/
        new Client("127.0.0.1",2232).run();
    }
}
