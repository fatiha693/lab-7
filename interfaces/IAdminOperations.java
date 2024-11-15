package interfaces;

import models.User;
import models.UserType;

public interface IAdminOperations extends IUserWriter {
    boolean updateUserPrivileges(String userId, UserType newType);
    boolean modifySystemSettings(String operation, String value);
}