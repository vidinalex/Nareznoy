<?php
    $con = mysqli_connect("vidinalex.000webhostapp.com", "id5494551_vidinalex", "nareznoy1love", "id5494551_nareznoy");
    
    $username = $_POST["username"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE username = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $username, $password);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $name, $email, $password, $qrcode);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["name"] = $name;
        $response["email"] = $email;
        $response["password"] = $password;
        $response["qrcode"] = $qrcode;
    }
    
    echo json_encode($response);
?>
