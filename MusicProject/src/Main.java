import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner terminalInput = new Scanner(System.in);
        String PilihanUser;
        Boolean isLanjutkan = true;

        while(isLanjutkan){
            clearScreen();
            System.out.println("Music Application By Maltzu");
            System.out.println("\t1.List Seluruh Music");
            System.out.println("\t2.Cari Music");
            System.out.println("\t3.Tambah Music");
            System.out.println("\t4.Ubah Music");
            System.out.println("\t5.Hapus Music");
            System.out.print("\t6.Putar Music");

            System.out.print("\n\nPilihan Anda : ");
            PilihanUser = terminalInput.next();

            switch (PilihanUser){
                case "1":
                    System.out.println("\n==================");
                    System.out.println("List Seluruh Music");
                    System.out.println("==================");
                    TampilkanMusic();
                    break;
                case "2":
                    System.out.println("\n===========");
                    System.out.println("Cari Music");
                    System.out.println("===========");
                    SearchMusic();
                    break;
                case "3":
                    System.out.println("\n============");
                    System.out.println("Tambah Music");
                    System.out.println("============");
                    AddMusic();
                    break;
                case "4":
                    System.out.println("\n==========");
                    System.out.println("Ubah Music");
                    System.out.println("==========");
                    ChangeMusic();
                    break;
                case "5":
                    System.out.println("\n===========");
                    System.out.println("Hapus Music");
                    System.out.println("===========");
                    DeleteMusic();
                    break;
                case "6":
                    System.out.println("\n=========");
                    System.out.println("Play Song");
                    System.out.println("=========");
                    PlaySong();
                    break;
                default:
                    System.out.println("\nInput anda tidak ditemukan \nSilahkan pilih [1-6]");
                    break;
            }
                isLanjutkan = getYesOrNo("Apakah Anda Ingin Melanjutkan");
        }
    }

    private static void TampilkanMusic() throws IOException{

        FileReader fileInput;
        BufferedReader bufferedInput;

        try{
            fileInput = new FileReader("Musicbase.txt");
            bufferedInput = new BufferedReader(fileInput);
        }catch (Exception e){
            System.err.println("Database tidak ditemukan");
            System.err.println("Silahkan tambah data terlebih dahulu");
            AddMusic();
            return;
        }

        System.out.println("\n| No |\tJudul                              |\tPenyanyi                                  ");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        String Data = bufferedInput.readLine();
        int nomorData = 0;
        while(Data != null){
            nomorData++;

            StringTokenizer stringToken = new StringTokenizer(Data,",");
            System.out.printf("| %2d ",nomorData);
            System.out.printf("| %-35s ",stringToken.nextToken());
            System.out.printf("| %-30s ",stringToken.nextToken());
            System.out.print("\n");

            Data = bufferedInput.readLine();
        }
        System.out.println("----------------------------------------------------------------------------------------------------------");
    }

    private static void AddMusic() throws IOException{
        FileWriter fileoutput = new FileWriter("Musicbase.txt",true);
        BufferedWriter bufferedoutput = new BufferedWriter(fileoutput);


        // mengambil input dari user
        Scanner terminalInput = new Scanner(System.in);
        String Judul,Penyanyi,URLLINK;

        System.out.print("Masukkan Judul: ");
        Judul = terminalInput.nextLine();
        System.out.print("Masukkan nama Penyanyi: ");
        Penyanyi = terminalInput.nextLine();
        System.out.print("Masukkan URL: ");
        URLLINK = terminalInput.nextLine();

        // cek buku di database
        String keywords[] = {Judul+","+Penyanyi+","+URLLINK};
        System.out.println(Arrays.toString(keywords));

        boolean isExist = cekMusicDiMusicBase(keywords,false);

        if(!isExist) {
            // Menulis Buku Di Database
            System.out.println("\nMusic Yang Akan Anda Masukkan Adalah");
            System.out.println("----------------------------------------");
            System.out.println("Judul    : " + Judul);
            System.out.println("Penyanyi : " + Penyanyi);
            System.out.println("URL      : " + URLLINK);

            boolean isTambah = getYesOrNo("Apakah Anda Ingin Menambah Data Tersebut");

            if (isTambah) {
                bufferedoutput.write(Judul + "," + Penyanyi + "," + URLLINK);
                bufferedoutput.newLine();
                bufferedoutput.flush();
            }

        }else{
            System.out.println("Music yang anda tambahkan sudah ada di dalam Musicbase:");
            cekMusicDiMusicBase(keywords,true);
        }

        bufferedoutput.close();
    }

    private static void PlaySong() throws IOException{

        try{
            File file = new File("Musicbase.txt");
        }catch (Exception e){
            System.err.println("Database tidak ditemukan");
            System.err.println("Silahkan tambah data terlebih dahulu");
            AddMusic();
            return;
        }

        FileReader fileinput = new FileReader("Musicbase.txt");
        BufferedReader bufferedInput = new BufferedReader(fileinput);

        Scanner terminalInput = new Scanner(System.in);

        System.out.println("List Music");
        TampilkanMusic();

        System.out.print("\nMasukkan Nomor Music Yang Akan Di Putar: ");
        int PlayMusic = terminalInput.nextInt();

        String data = bufferedInput.readLine();
        int entryCounts = 0;

        while (data != null){
            entryCounts++;


            StringTokenizer st = new StringTokenizer(data,",");
            st.nextToken();
            st.nextToken();
            String LINKYT = st.nextToken();
            if(entryCounts == PlayMusic){
                new ProcessBuilder("cmd","/c","start ",LINKYT).inheritIO().start();
            }
            data = bufferedInput.readLine();
        }
    }

    private static void ChangeMusic() throws IOException{
        // Kita Ambil Data Base Original
        File database = new File("Musicbase.txt");
        FileReader fileinput = new FileReader("Musicbase.txt");
        BufferedReader bufferedinput = new BufferedReader(fileinput);

        // Kita Buat Database Sementara
        File TempDB = new File("TempDB.txt");
        FileWriter fileOutput = new FileWriter("TempDB.txt");
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // tampilkan data
        System.out.println("List Music");
        TampilkanMusic();

        // ambil user input / pilihan Data
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\nMasukkan Nomor Music Yang Akan Di ubah : ");
        int UpdateNUM = terminalInput.nextInt();

        // tampilkan data yang ingin di Update

        String data = bufferedinput.readLine();
        int entryCounts = 0;

        while (data != null){
            entryCounts++;

            StringTokenizer stringTokenizer = new StringTokenizer(data,",");

            // tampilkan entryCounts == UpdateNUM
            if(entryCounts == UpdateNUM){
                System.out.println("\nMusic Yang Ingin Anda Ubah Adalah:");
                System.out.println("---------------------------------------");
                System.out.println("Judul    : " + stringTokenizer.nextToken());
                System.out.println("Penyanyi : " + stringTokenizer.nextToken());
                System.out.println("URL      : " + stringTokenizer.nextToken());

                // change music

                // mengambil input dari user
                String[] fieldData = {"Judul","Penyanyi","URL"};
                String[] tempData = new String[3];

                stringTokenizer = new StringTokenizer(data,",");
                String originalData;

                for(int i = 0;i < fieldData.length;i++){
                    boolean isChange = getYesOrNo("Apakah Anda Ingin Merubah " + fieldData[i]);
                    originalData = stringTokenizer.nextToken();
                    if(isChange){
                        // user input

                        terminalInput = new Scanner(System.in);
                        System.out.print("\nMasukkan " + fieldData[i] + " baru : ");
                        tempData[i] = terminalInput.nextLine();
                    } else {
                        tempData[i] = originalData;
                    }
                }

                // tampilkan Data Baru Ke layar
                stringTokenizer = new StringTokenizer(data,",");
                System.out.println("\nData baru anda adalah ");
                System.out.println("Judul    : " + stringTokenizer.nextToken() + " --> " + tempData[0]);
                System.out.println("Penyanyi : " + stringTokenizer.nextToken() + " --> " + tempData[1]);
                System.out.println("URL      : " + stringTokenizer.nextToken() + " --> " + tempData[2]);

                boolean isUpdate = getYesOrNo("Apakah Anda Yakin Ingin Mengupdate Data Tersebut");

                if (isUpdate){

                    boolean isExist = cekMusicDiMusicBase(tempData,false);

                    if(isExist){
                        System.err.println("Music Sudah Ada Di Musicbase, proses change dibatalkan, \nSilahkan delete music yang bersangkutan");
                        // copy data
                        bufferedOutput.write(data);
                    } else {

                        // format baru ke music baru ke musicbase
                        String judul = tempData[0];
                        String Penyanyi = tempData[1];
                        String URL = tempData[2];

                        // tulis music ke musicbase
                        bufferedOutput.write(judul+","+Penyanyi+","+URL);
                    }
                } else {
                    // copy data
                    bufferedOutput.write(data);
                }
            } else {
                // copy data
                bufferedOutput.write(data);
            }
            bufferedOutput.newLine();

            data = bufferedinput.readLine();
        }

        // menulis data ke file
        bufferedOutput.flush();
        fileinput.close();
        bufferedinput.close();
        fileOutput.close();
        bufferedOutput.close();
        System.gc();
        // delete data original database
        database.delete();

        // rename file TempDB to Musicbase
        TempDB.renameTo(database);
    }

    private static void DeleteMusic() throws IOException{
        // kita ambil musicbase Original
        File Database = new File("Musicbase.txt");
        FileReader fileinput = new FileReader("Musicbase.txt");
        BufferedReader bufferedinput = new BufferedReader(fileinput);

        // kita buat database sementara
        File TempDB = new File("TempDB.txt");
        FileWriter fileOutput = new FileWriter("TempDB.txt");
        BufferedWriter bufferedOutput = new BufferedWriter(fileOutput);

        // tampilkan Data
        System.out.println("List Music");
        TampilkanMusic();

        // kita ambil input dari user untuk delete music
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("Masukkan nomor Music Yang Akan Di Hapus: ");
        int DeleteNum = terminalInput.nextInt();

        // looping membaca tiap data baris dan skip data yang akan di hapus

        boolean isFound = false;
        int EntryCounts = 0;

        String data = bufferedinput.readLine();

        while (data != null) {
            EntryCounts++;
            boolean isDelete = false;

            StringTokenizer stringTokenizer = new StringTokenizer(data, ",");

            // tampilkan entryCounts == UpdateNUM
            if (EntryCounts == DeleteNum) {
                System.out.println("\nMusic Yang Ingin Anda Hapus Adalah:");
                System.out.println("---------------------------------------");
                System.out.println("Judul    : " + stringTokenizer.nextToken());
                System.out.println("Penyanyi : " + stringTokenizer.nextToken());
                System.out.println("URL      : " + stringTokenizer.nextToken());

                isDelete = getYesOrNo("Apakah anda yakin ingin menghapus");
                isFound = true;
            }

            if(isDelete){
                // skip pindahkan data dari original ke sementara
                System.out.println("Data Berhasil Dihapus");
            } else {
                // kita pindahkan data dari original ke sementara
                bufferedOutput.write(data);
                bufferedOutput.newLine();
            }
            data = bufferedinput.readLine();
        }

        if (!isFound){
            System.err.println("Buku tidak ditemukan");
        }

        // menulis data ke file
        bufferedOutput.flush();
        fileinput.close();
        bufferedinput.close();
        fileOutput.close();
        bufferedOutput.close();
        System.gc();
        // delete original data
        Database.delete();

        // rename file sementara ke database
        TempDB.renameTo(Database);

    }

    private static void SearchMusic() throws IOException{
        // membaca database ada atau tidak

        try{
            File file = new File("Musicbase.txt");
        }catch (Exception e){
            System.err.println("Database tidak ditemukan");
            System.err.println("Silahkan tambah data terlebih dahulu");
            AddMusic();
            return;
        }

        // kita ambil keyword dari user

        Scanner terminalInput =  new Scanner(System.in);
        System.out.print("Masukkan Kata Kunci Untuk Mencari Music :");
        String cariString = terminalInput.nextLine();
        String[] keywords = cariString.split("\\s+");

        // kita check keyword di database

        cekMusicDiMusicBase(keywords,true);
    }

    private static boolean cekMusicDiMusicBase(String[] keywords, boolean isDisplay) throws IOException{

        FileReader fileInput = new FileReader("Musicbase.txt");
        BufferedReader bufferedInput = new BufferedReader(fileInput);

        if(isDisplay) {
            System.out.println("\n| No |\tJudul                         |\tPenyanyi                                  ");
            System.out.println("----------------------------------------------------------------------------------------------------------");
        }

        String Data = bufferedInput.readLine();
        boolean isExist = false;
        int nomorData = 0;
        while(Data != null){

            isExist = true;

            for(String keyword:keywords){
                isExist = isExist && Data.toLowerCase().contains(keyword.toLowerCase());
            }

            if(isExist) {
                if (isDisplay) {
                    nomorData++;
                    StringTokenizer stringToken = new StringTokenizer(Data, ",");
                    System.out.printf("| %2d ", nomorData);
                    System.out.printf("| %-30s ", stringToken.nextToken());
                    System.out.printf("| %-30s ", stringToken.nextToken());
                    System.out.print("\n");

                } else {
                    break;
                }
            }
            Data = bufferedInput.readLine();
        }
        if(isDisplay) {
            System.out.println("----------------------------------------------------------------------------------------------------------");
        }
        return isExist;
    }

    private static boolean getYesOrNo(String message){
        Scanner terminalInput = new Scanner(System.in);
        System.out.print("\n"+message+" (y/n) ? ");
        String pilihanUser = terminalInput.next();

        while (!pilihanUser.equalsIgnoreCase("y") && !pilihanUser.equalsIgnoreCase("n")){
            System.err.println("Pilihan anda bukan y atau n ");
            System.out.print("\n"+message+" (y/n) ? ");
            pilihanUser = terminalInput.next();
        }

        return pilihanUser.equalsIgnoreCase("y");

    }

    private static void clearScreen(){
        try{
            if(System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            }else{
                System.out.print("\033\143");
            }
        }catch (Exception ex){
            System.err.println("Tidak bisa clear screen");
        }
    }
}


