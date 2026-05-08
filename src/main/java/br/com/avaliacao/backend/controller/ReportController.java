package br.com.avaliacao.backend.controller;

import br.com.avaliacao.backend.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Defeito intencional: endpoint administrativo sem autenticacao/autorizacao.
    @GetMapping("/summary")
    public Map<String, Object> summary() {
        return reportService.summary();
    }
}
