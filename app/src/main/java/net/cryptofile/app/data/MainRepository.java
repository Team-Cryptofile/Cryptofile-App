package net.cryptofile.app.data;

public class MainRepository {

    private static volatile ServerDataSource dataSource;

    private static volatile MainRepository instance;

    public MainRepository(ServerDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static MainRepository getInstance(ServerDataSource dataSource) {
        if (instance == null) {
            instance = new MainRepository(dataSource);
        }
        return instance;
    }

    public Result uploadFile(byte[] file, String title, String filetype) throws Exception {
        Result result = dataSource.uploadFile(file, title, filetype);
        if (result instanceof Result.Success) {
            System.out.println("Succsessfully uploaded");
        }
        return result;
    }
}
