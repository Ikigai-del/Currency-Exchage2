package org.example.repository;

import org.example.Entity.CurrencyEntity;
import org.example.Entity.ExchangeRate;
import org.example.Utils.DataBase;
import org.example.exception.DataBaseOperationErrorException;
import org.example.exception.EntityExistException;
import org.example.exception.NotFoundException;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateRepositoryImpl implements ExchangeRepository {
    private final CurrencyRepository currencyRepository = new CurrencyRepositoryImpl();

    @Override
    public List<ExchangeRate> selectAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String sql = "select * from ExchangeRates";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long baseCurId = resultSet.getLong("base_currency_id");
                long targetCurId = resultSet.getLong("target_currency_id");
                BigDecimal rate = resultSet.getBigDecimal("rate");
                Optional<CurrencyEntity> baseCur = currencyRepository.findById(baseCurId);
                Optional<CurrencyEntity> targetCur = currencyRepository.findById(targetCurId);
                if (baseCur.isPresent() && targetCur.isPresent()) {
                    exchangeRates.add(new ExchangeRate(id, baseCur.get(), targetCur.get(), rate));
                }
            }

        } catch (SQLException e) {
            throw new DataBaseOperationErrorException("select exchange error" + e.getMessage());
        }
        return exchangeRates;
    }

    @Override
    public ExchangeRate save(ExchangeRate entity) {
        String sql = "insert into ExchangeRates (base_currency_id, target_currency_id, rate) values (?, ?, ?)";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, entity.getBaseCurrency().getId());
            statement.setLong(2, entity.getTargetCurrency().getId());
            statement.setBigDecimal(3, entity.getRate());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            if (e instanceof SQLiteException) {
                SQLiteException sqLiteException = (SQLiteException) e;
                if (sqLiteException.getResultCode().code == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                    throw new EntityExistException(
                            String.format("Exchange rate from %s to %s already exists",
                                    entity.getBaseCurrency().getCode(),
                                    entity.getTargetCurrency().getCode()));

                }
            }
            throw new DataBaseOperationErrorException("Insert exchange rate was failed " + e.getMessage());
        }
    }


    @Override
    public void delete(Long id) {
        String query = "delete from ExchangeRates where id = ?";

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
    public Optional<ExchangeRate> findByCodes(String baseCurCode, String targetCurCode) {
        long baseId, targetId;
        Optional<CurrencyEntity> baseCur = currencyRepository.findByCode(baseCurCode);
        Optional<CurrencyEntity> targetCur = currencyRepository.findByCode(targetCurCode);

        if (baseCur.isPresent() && targetCur.isPresent()) {
            baseId = baseCur.get().getId();
            targetId = targetCur.get().getId();
        } else {
            throw new NotFoundException("There was no currencies by this codes id Data Base");
        }

        String query = "SELECT * FROM ExchangeRates WHERE base_currency_id = ? AND target_currency_id = ?";

        try (Connection connection = DataBase.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, baseId);
            statement.setLong(2, targetId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ExchangeRate exchangeRate = new ExchangeRate(
                        resultSet.getLong("id"),
                        baseCur.get(),
                        targetCur.get(),
                        resultSet.getBigDecimal("rate")
                );
                return Optional.of(exchangeRate);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new DataBaseOperationErrorException("Error finding Exchange Rate by codes");
        }
    }

    @Override
    public void updateRate(ExchangeRate exchangeRate) {
        String sql = "update ExchangeRates set rate= ?  where id = ?";
        try (Connection connection = DataBase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, exchangeRate.getRate());
            statement.setLong(2, exchangeRate.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseOperationErrorException("Update failed: ");
        }

    }
}