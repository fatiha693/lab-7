package interfaces;

import models.User;

public interface IUserWriter extends IUserReader {
    boolean addUser(User newUser);
}