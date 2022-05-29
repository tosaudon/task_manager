package jp.gihyo.projava.tasklist;
import jp.gihyo.projava.tasklist.HomeRestCon.TaskItem;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TaskListDoa {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    TaskListDoa(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(TaskItem taskItem){
        SqlParameterSource param = new BeanPropertySqlParameterSource(taskItem);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("tasklist");
        insert.execute(param);
    }

    public List<TaskItem> findAll(){
        String query = "SELECT * FROM tasklist";

        List<Map<String,Object>> result = jdbcTemplate.queryForList(query);
        List<TaskItem> taskItems = result.stream()
                .map((Map<String, Object> row) -> new TaskItem(
                        row.get("id").toString(),
                        row.get("task").toString(),
                        row.get("note").toString(),
                        row.get("deadline").toString(),
                        (Boolean)row.get("done"))).toList();

        return taskItems;
    }
    public int delete(String id){
        int number = jdbcTemplate.update("DELETE FROM tasklist WHERE id = ?",id);
        return number;
    }
    public int update(TaskItem taskItem){
        int number = jdbcTemplate.update(
                "UPDATE tasklist SET task = ?, note = ?,deadline = ?, done = ? WHERE id = ?",
                taskItem.task(),
                taskItem.note(),
                taskItem.deadline(),
                taskItem.done(),
                taskItem.id());
            return number;
    }
    public int filter(String deadline){
        Date date=new Date();
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate=dateFormat.format(date);
        int number = jdbcTemplate.update("select * FROM tasklist WHERE deadline > ?",deadline);
        return number;
    }
}
