package com.example.jobstogo

data class Product(var  productid:String,var vendorid:String ?= null,var productname:String ?= null,var productprice:Double ?= null,var productdescription:String ?= null) {
}