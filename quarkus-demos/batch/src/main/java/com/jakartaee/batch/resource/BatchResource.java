package com.jakartaee.batch.resource;

import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.batch.runtime.JobExecution;
import jakarta.batch.runtime.JobInstance;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Recurso REST para gestionar jobs batch.
 * Demuestra el uso de Jakarta Batch API.
 */
@Path("/api/batch")
@Produces(MediaType.APPLICATION_JSON)
public class BatchResource {
    
    private final JobOperator jobOperator;
    
    public BatchResource() {
        this.jobOperator = BatchRuntime.getJobOperator();
    }
    
    /**
     * Lista todos los nombres de jobs disponibles.
     * GET /api/batch/jobs
     */
    @GET
    @Path("/jobs")
    public Response listJobs() {
        // En un caso real, obtendrías los nombres de jobs desde configuración
        List<String> jobNames = List.of(
            "import-heroes",
            "power-statistics",
            "hero-report"
        );
        return Response.ok(jobNames).build();
    }
    
    /**
     * Inicia un job batch.
     * POST /api/batch/jobs/{jobName}/start
     */
    @POST
    @Path("/jobs/{jobName}/start")
    public Response startJob(@PathParam("jobName") String jobName) {
        try {
            Properties jobParameters = new Properties();
            long executionId = jobOperator.start(jobName, jobParameters);
            
            return Response.ok()
                    .entity("Job started with execution ID: " + executionId)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error starting job: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * Obtiene información de una ejecución de job.
     * GET /api/batch/jobs/executions/{executionId}
     */
    @GET
    @Path("/jobs/executions/{executionId}")
    public Response getJobExecution(@PathParam("executionId") long executionId) {
        try {
            JobExecution execution = jobOperator.getJobExecution(executionId);
            if (execution == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Job execution not found: " + executionId)
                        .build();
            }
            
            JobExecutionInfo info = new JobExecutionInfo(
                execution.getExecutionId(),
                execution.getJobName(),
                execution.getBatchStatus().toString(),
                execution.getStartTime(),
                execution.getEndTime()
            );
            
            return Response.ok(info).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error getting job execution: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * Lista todas las ejecuciones de un job.
     * GET /api/batch/jobs/{jobName}/executions
     */
    @GET
    @Path("/jobs/{jobName}/executions")
    public Response getJobExecutions(@PathParam("jobName") String jobName) {
        try {
            List<JobInstance> instances = jobOperator.getJobInstances(jobName, 0, 100);
            
            List<JobExecutionInfo> executions = instances.stream()
                    .flatMap(instance -> {
                        List<JobExecution> execs = jobOperator.getJobExecutions(instance);
                        return execs.stream().map(exec -> new JobExecutionInfo(
                            exec.getExecutionId(),
                            exec.getJobName(),
                            exec.getBatchStatus().toString(),
                            exec.getStartTime(),
                            exec.getEndTime()
                        ));
                    })
                    .collect(Collectors.toList());
            
            return Response.ok(executions).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error getting job executions: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * Detiene una ejecución de job.
     * POST /api/batch/jobs/executions/{executionId}/stop
     */
    @POST
    @Path("/jobs/executions/{executionId}/stop")
    public Response stopJobExecution(@PathParam("executionId") long executionId) {
        try {
            jobOperator.stop(executionId);
            return Response.ok("Job execution stopped: " + executionId).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error stopping job execution: " + e.getMessage())
                    .build();
        }
    }
    
    /**
     * Clase interna para información de ejecución de job.
     */
    public static class JobExecutionInfo {
        private long executionId;
        private String jobName;
        private String batchStatus;
        private java.util.Date startTime;
        private java.util.Date endTime;
        
        public JobExecutionInfo(long executionId, String jobName, String batchStatus,
                              java.util.Date startTime, java.util.Date endTime) {
            this.executionId = executionId;
            this.jobName = jobName;
            this.batchStatus = batchStatus;
            this.startTime = startTime;
            this.endTime = endTime;
        }
        
        // Getters
        public long getExecutionId() { return executionId; }
        public String getJobName() { return jobName; }
        public String getBatchStatus() { return batchStatus; }
        public java.util.Date getStartTime() { return startTime; }
        public java.util.Date getEndTime() { return endTime; }
    }
}

