package jp.gihyo.projava.tasklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeRestCon {

    record TaskItem(String id, String task, String note, String deadline, boolean done ){}
    private List<TaskItem> taskItems = new ArrayList<>();
    private final TaskListDoa dao;


    @Autowired
    HomeRestCon (TaskListDoa dao) {
        this.dao = dao;
    }

    @GetMapping("/list")
    String list(Model model){
        List<TaskItem> taskItems = dao.findAll();
        model.addAttribute("taskList", taskItems);
        return "home";
    }

    @GetMapping("/add")
    String addItem(@RequestParam("task") String task,
                   @RequestParam("note") String note,
                   @RequestParam("deadline") String deadline){
        String id = UUID.randomUUID().toString().substring(0,8);
        TaskItem item = new TaskItem(id, task, note, deadline, false);
        dao.add(item);

        return "redirect:/list";

    }

    @GetMapping("/delete")
    String delete(@RequestParam("id") String id){
        dao.delete(id);
        return "redirect:/list";
    }

    @GetMapping("/update")
    String updateItem(@RequestParam("id") String id,
                      @RequestParam("task") String task,
                      @RequestParam("note") String note,
                      @RequestParam("deadline") String deadline,
                      @RequestParam("done") boolean done){
        TaskItem taskItem = new TaskItem(id, task, note, deadline, done);
        dao.update(taskItem);
        return "redirect:/list";
    }
    @GetMapping("/filter")
    String filter(@RequestParam("deadline") String deadline){
        dao.filter(deadline);
        return "redirect:/list";
    }
}
