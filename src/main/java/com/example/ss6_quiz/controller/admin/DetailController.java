package com.example.ss6_quiz.controller.admin;

import com.example.ss6_quiz.entity.AdminLogs;
import com.example.ss6_quiz.projection.CountStatsProjection;
import com.example.ss6_quiz.service.IAdminLogsService;
import com.example.ss6_quiz.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/details")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
public class DetailController {
    @Autowired
    private IAdminLogsService adminLogsService;
    @Autowired
    private IReportService reportService;

    @GetMapping("/log")
    public List<AdminLogs> getLogs(
            @RequestParam(value = "actionType", defaultValue = "") String actionType
    ){
        return adminLogsService.getLogs(actionType);
    }

    @GetMapping("/count")
    public CountStatsProjection getStats(){
        return adminLogsService.getCountStats();
    }

    @PutMapping("/report/{id}")
    public ResponseEntity<String> solve(@PathVariable Long id) {
        int result = reportService.solveReport(id);
        if (result > 0) {
            return ResponseEntity.ok("Đã thực hiện");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/report/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reportService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/log/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        adminLogsService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
