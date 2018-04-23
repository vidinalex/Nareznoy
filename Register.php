<?php
    $con = mysqli_connect("vidinalex.000webhostapp.com", "id5494551_vidinalex", "nareznoy1love", "id5494551_nareznoy");
    
    $name = $_POST["name"];
    $email = $_POST["email"];
    $password = $_POST["password"];

    $statement = mysqli_prepare($con, "INSERT INTO user (name, email, password) VALUES (?, ?, ?)");
    mysqli_stmt_bind_param($statement, "siss", $name, $email, $password);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    print_r(json_encode($response));
?>
