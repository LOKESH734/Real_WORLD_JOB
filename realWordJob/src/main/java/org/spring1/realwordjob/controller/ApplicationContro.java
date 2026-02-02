//package org.spring1.realwordjob.controller;
//
//import jakarta.annotation.PostConstruct;
//import org.spring1.realwordjob.model.Application;
//import org.spring1.realwordjob.model.Job;
//import org.spring1.realwordjob.model.User;
//import org.spring1.realwordjob.service.ApplicationService;
//import org.spring1.realwordjob.service.JobService;
//import org.spring1.realwordjob.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Date;
//import java.util.List;
//
//@RestController
//@CrossOrigin
//@RequestMapping("/application")
//public class ApplicationContro {
//    @Autowired
//    private ApplicationService applicationService;
//    @Autowired
//    private JobService jobService;
//    @Autowired
//    private UserService userService;
//    private final String srcpath="uploads/";
//    @PostConstruct
//    public void init() {
//        try
//        {
//            Files.createDirectories(Paths.get(srcpath));
//        }
//        catch(Exception e)
//        {
//            throw new RuntimeException(e);
//        }
//    }
//    @PostMapping("/apply")
//    public String apply(@RequestParam long userid,
//                        @RequestParam long jobid,
//                        @RequestParam("resume")MultipartFile file)
//    {
//        User user=userService.findUser(userid);
//        Job job=jobService.findJob(jobid);
//        if(job==null||user==null) return "Null";
//        String FileName=System.currentTimeMillis()+"_"+file.getOriginalFilename();
//        Path filepath= Paths.get(srcpath,FileName);
//        try {
//            Files.copy(file.getInputStream(), filepath);
//        } catch (Exception e) {
//            return "Resume Upload Failed: " + e.getMessage();
//        }
//
//
//        Application application=new Application();
//        application.setUser(user);
//        application.setJob(job);
//        application.setResumepath(filepath.toString());
//        application.setApplieddate(new Date());
//        applicationService.save(application);
//        return "Success";
//
//    }
//    @GetMapping("/findall")
//    public List<Application> getAll()
//    {
//       return  applicationService.getAll();
//    }
//    @GetMapping("/find/{id}")
//    public List<Application> find(@PathVariable long id)
//    {
//        return applicationService.getByJobId(id);
//
//    }
//}
