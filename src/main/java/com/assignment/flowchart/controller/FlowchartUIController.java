package com.assignment.flowchart.controller;

import com.assignment.flowchart.domain.Flowchart;
import com.assignment.flowchart.dto.FlowchartDTO;
import com.assignment.flowchart.service.FlowchartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/flowcharts/ui")
public class FlowchartUIController {

    @Autowired
    private FlowchartService service;

    @GetMapping
    public String listFlowcharts(Model model) {
        model.addAttribute("flowcharts", service.getAllFlowcharts());
        return "flowcharts";
    }

    @GetMapping("/{id}")
    public String viewFlowchart(@PathVariable Long id, Model model) {
        Flowchart flowchart = service.getFlowchartById(id);
        model.addAttribute("flowchart", flowchart);
        return "view-flowchart";
    }

    @GetMapping("/create")
    public String createFlowchartForm(Model model) {
        model.addAttribute("flowchart", new Flowchart());
        return "create-flowchart";
    }

    @PostMapping("/create")
    public String createFlowchart(@ModelAttribute FlowchartDTO flowchart) {
        service.createFlowchart(flowchart);
        return "redirect:/flowcharts/ui";
    }

    @GetMapping("/{id}/edit")
    public String editFlowchartForm(@PathVariable Long id, Model model) {
        Flowchart flowchart = service.getFlowchartById(id);
        model.addAttribute("flowchart", flowchart);
        return "edit-flowchart";
    }

//    @PostMapping("/{id}/edit")
//    public String updateFlowchart(@PathVariable Long id, @ModelAttribute Flowchart flowchart) {
//        service.updateFlowchart(id, flowchart);
//        return "redirect:/flowcharts/ui";
//    }

    @PostMapping("/{id}/delete")
    public String deleteFlowchart(@PathVariable Long id) {
        service.deleteFlowchart(id);
        return "redirect:/flowcharts/ui";
    }
}
