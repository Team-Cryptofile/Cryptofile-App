package net.cryptofile.app.data;

import java.io.File;

public class MainRepository {

    private static volatile ServerDataSource dataSource;

    private static volatile MainRepository instance;

    public MainRepository(ServerDataSource dataSource) {
        MainRepository.dataSource = dataSource;
    }

    public static MainRepository getInstance(ServerDataSource dataSource) {
        if (instance == null) {
            instance = new MainRepository(dataSource);
        }
        return instance;
    }

    public Result uploadFile(File file, String title, String filetype) {
        Result result = dataSource.uploadFile(file, title, filetype);
        if (result instanceof Result.Success) {
            System.out.println("Succsessfully uploaded");
        }
        return result;
    }
}
