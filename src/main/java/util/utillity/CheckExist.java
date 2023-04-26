package util.utillity;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CheckExist {

    public static void checkNotNullUserIdAndFileName(String maybeUserId, String fileName) {
        if (maybeUserId == null) {
            throw new RuntimeException("Вы не ввели id пользователя");
        }
        if (fileName == null) {
            throw new RuntimeException("Вы не ввели имя файла");
        }
    }
}
