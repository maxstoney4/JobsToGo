package com.example.jobstogo

data class Job(var  jobid:String, var vendorid:String ?= null, var jobname:String ?= null, var joblocation:String ?= null, var jobdescription:String ?= null) {
}