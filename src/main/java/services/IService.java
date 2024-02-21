package services;

import java.sql.Connection;
import java.sql.SQLException;

public interface IService<T> {
    void registerUser(Connection connection, T user) throws SQLException;
}
