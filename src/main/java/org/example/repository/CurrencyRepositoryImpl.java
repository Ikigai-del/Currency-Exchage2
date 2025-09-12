package org.example.repository;

import org.example.Entity.CurrencyEntity;
import org.example.Utils.DataBase;
import org.example.exception.DataBaseOperationErrorException;
import org.example.exception.EntityExistException;
import org.example.exception.NotFoundException;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyRepositoryImpl implements CurrencyRepository {

    @Override
    public List<CurrencyEntity> selectAll() {
        List<CurrencyEntity> currencies = new ArrayList<>();
        String sql = "select * from Currencies";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String code = resultSet.getString("code");
                String full_name = resultSet.getString("sign");
                String sign = resultSet.getString("sign");
                currencies.add(new CurrencyEntity(id, code, full_name, sign));
            }
            return currencies;
        } catch (Exception e) {
            throw new DataBaseOperationErrorException("select currencies error " + e.getMessage());
        }

    }

    @Override
    public CurrencyEntity save(CurrencyEntity currency) {
        String query = "insert into Currencies (code, full_name, sign) values (?, ?, ?)";

        try (Connection connection = DataBase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query , PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();
            return currency;
        } catch (SQLException e) {
            if (e instanceof SQLiteException) {
                SQLiteException exception = (SQLiteException) e;
                if (exception.getResultCode().code == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                    throw new EntityExistException("Currency with code '" + currency.getCode() + "' already exists");
                }
            }
            throw new DataBaseOperationErrorException("Save entity is failed: " + e.getMessage());
        }
    }


    @Override
    public void delete(Long id) {

        String query = "delete from Currencies where id = ?";

        try (Connection connection = DataBase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            statement.executeUpdate();

            System.out.println("Entity deleted");
        } catch (SQLException e) {
            throw new DataBaseOperationErrorException("Delete failed: " + e.getMessage());
        }

    }

    @Override
    public Optional<CurrencyEntity> findById(long id) {

        String query = "select * from Currencies where id = ?";

        try (Connection connection = DataBase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                long Id = resultSet.getLong("id");
                String code = resultSet.getString("code");
                String fullName = resultSet.getString("full_name");
                String sign = resultSet.getString("sign");
                CurrencyEntity entity = new CurrencyEntity(Id, code, fullName, sign);
                return Optional.of(entity);

            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new NotFoundException("There is no entity with id " + id + " in DB");
        }

    }

    @Override
    public Optional<CurrencyEntity> findByCode(String code) {

        String query = "select * from Currencies where code = ?";

        try (Connection connection = DataBase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, code.toUpperCase());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                long ID = resultSet.getLong("id");
                String full_name = resultSet.getString("full_name");
                String Sign = resultSet.getString("sign");
                CurrencyEntity entity = new CurrencyEntity(ID, code.toUpperCase(), full_name, Sign);
                return Optional.of(entity);

            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new NotFoundException("There is no entity with code " + code + " in DB");
        }
    }
}
