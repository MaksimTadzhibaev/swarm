import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.LinkedList;

public class ClientMessageHandler extends ChannelInboundHandlerAdapter {
    private static String pathOfDirectories = "";
    private static String pathsOfDirectories = "";
    private static LinkedList<File> list = new LinkedList<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null) {
            return;
        }
//        регистрация
        else if (msg instanceof RegistrationMsg) {
            RegistrationMsg registrationMsg = (RegistrationMsg) msg;
            DBHandler.getConnectionWithDB();
            if (DBHandler.checkIfUserExistsForAuthorization(registrationMsg.getLogin())) {
                ctx.writeAndFlush("userAlreadyExists");
            } else {
                if (DBHandler.registerNewUser(registrationMsg.getLogin(), registrationMsg.getPassword())) {
                    File newDirectory = new File("ServerNetty/FileServer/" + registrationMsg.getLogin());
                    newDirectory.mkdir();
                    ctx.writeAndFlush("registrationIsSuccessful");
                }
            }
            DBHandler.disconnectDB();
        }
//        аутентификация
        else if (msg instanceof AuthenticationMsg) {
            AuthenticationMsg authenticationMsg = (AuthenticationMsg) msg;
            DBHandler.getConnectionWithDB();
            if (DBHandler.checkIfUserExistsForAuthorization(authenticationMsg.getLogin())) {
                if (DBHandler.checkIfPasswordIsRight(authenticationMsg.getLogin(), authenticationMsg.getPassword())) {
                    ctx.writeAndFlush("userIsValid/" + authenticationMsg.getLogin());
                } else {
                    ctx.writeAndFlush("wrongPassword");
                }
            } else {
                ctx.writeAndFlush("userDoesNotExist");
            }
            DBHandler.disconnectDB();
        }
//        обновить
        else if (msg instanceof UpdateMsg) {
            UpdateMsg uploadMsg = (UpdateMsg) msg;
            String receivedLogin = uploadMsg.getLogin();
            ctx.writeAndFlush(new UpdateMsg(getContentsOfServerNetty(receivedLogin)));
        }
//        передача файла на сервер
        else if (msg instanceof FileToServerMsg) {
            FileToServerMsg fileToServerMsg = (FileToServerMsg) msg;
            Path pathToNewFile = Paths.get("ServerNetty/FileServer/" + fileToServerMsg.getLogin() + File.separator + fileToServerMsg.getFileName());
            if (fileToServerMsg.isDirectory() && fileToServerMsg.isEmpty()) {
                if (Files.exists(pathToNewFile)) {
                    System.out.println("Файл с таким именем уже существует");
                } else {
                    Files.createDirectory(pathToNewFile);
                }
            } else {
                if (Files.exists(pathToNewFile)) {
                    System.out.println("Файл с таким именем уже существует");
                } else {
                    Files.write(Paths.get("ServerNetty/FileServer/" + fileToServerMsg.getLogin() + File.separator + fileToServerMsg.getFileName()), fileToServerMsg.getData(), StandardOpenOption.CREATE);
                }
            }
            ctx.writeAndFlush(new UpdateMsg(getContentsOfServerNetty(fileToServerMsg.getLogin())));
        }
//        забрать файл с сервера
        else if (msg instanceof FileFromServerMsg) {
            FileFromServerMsg fileRequest = (FileFromServerMsg) msg;
            for (int i = 0; i < fileRequest.getFilesToRequest().size(); i++) {
                File file = new File(fileRequest.getFilesToRequest().get(i).getAbsolutePath());
                Path fileToRequest = Paths.get(fileRequest.getFilesToRequest().get(i).getAbsolutePath());
                try {
                    if (file.isDirectory()) {
                        if (file.listFiles().length == 0) {
                            ctx.writeAndFlush(new FileMsg(file.getName(), true, true));
                        } else {
                            ctx.writeAndFlush(new FileMsg(file.getName(), true, false));
                        }
                    } else {
                        try {
                            ctx.writeAndFlush(new FileMsg(fileToRequest));
                        } catch (AccessDeniedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        удаление файла
        else if (msg instanceof DeleteFileMsg) {
            // TODO: 31.05.2021 удаление файла с сервера 
        }
    }
    

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        DBHandler.disconnectDB();
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        DBHandler.disconnectDB();
        ctx.close();
    }

    public static HashMap<Integer, LinkedList<File>> getContentsOfServerNetty(String login) {
        HashMap<Integer, LinkedList<File>> cloudStorageContents;
        LinkedList<File> listCloudStorageFiles = new LinkedList<>();
        File path = new File("ServerNetty/FileServer/" + login);
        File[] files = path.listFiles();
        cloudStorageContents = new HashMap<>();
        if (files.length == 0) {
            cloudStorageContents.clear();
        } else {
            listCloudStorageFiles.clear();
            for (int i = 0; i < files.length; i++) {
                listCloudStorageFiles.add(files[i]);
            }
            cloudStorageContents.clear();
            cloudStorageContents.put(0, listCloudStorageFiles);
        }
        return cloudStorageContents;
    }
}