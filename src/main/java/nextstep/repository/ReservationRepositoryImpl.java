package nextstep.repository;

import static nextstep.domain.ThemeConstants.THEME_DESC;
import static nextstep.domain.ThemeConstants.THEME_NAME;
import static nextstep.domain.ThemeConstants.THEME_PRICE;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import nextstep.domain.ThemeConstants;
import nextstep.dto.ReservationRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {


    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    public ReservationRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;

    }

    @Override
    public Long save(ReservationRequestDTO reservationRequestDTO) throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        String sql = "INSERT INTO reservation (date, time, name, theme_name, theme_desc, theme_price) VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
        ps.setDate(1, Date.valueOf(reservationRequestDTO.getDate()));
        ps.setTime(2, Time.valueOf(reservationRequestDTO.getTime()));
        ps.setString(3, reservationRequestDTO.getName());
        ps.setString(4, THEME_NAME);
        ps.setString(5, THEME_DESC);
        ps.setInt(6, THEME_PRICE);
        ResultSet resultSet = ps.executeQuery(sql);
        Long id = null;

        if (resultSet.next()) {
            id = resultSet.getLong(1);
        }

        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            System.err.println("rs 오류:" + e.getMessage());
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            System.err.println("ps 오류:" + e.getMessage());
        }
        DataSourceUtils.releaseConnection(connection, dataSource);
        return id;

    }
}