package interfaces;

import java.util.List;
import models.User;

public interface IUserReader {
    List<User> viewUsers();
}