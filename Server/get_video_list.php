<?php
include 'mysql_header.php';

$sql;
switch ($_GET['key']) {
    case 'latest':
        $sql="select * from videos order by id desc limit {$_GET['seek']}, 10";
        hand_result($conn->query($sql));
        break;

    case 'top':
        $sql="select * from videos order by count DESC limit 10";
        hand_result($conn->query($sql));
        break;
    
    case 'random':
        $sql="select * from videos order by rand() limit 10";
        hand_result($conn->query($sql));        
        break;
    default:
        echo $_GET['key'];
        break;
}

function hand_result($result){
    if (mysqli_num_rows($result) > 0) {
        // 输出数据
        while($row = mysqli_fetch_assoc($result)) {
            $item=array(
                'id'=>$row['id'],
                'filename'=>$row['filename'],
                'videoname'=>$row['videoname'],
                'count'=>$row['count'],
                'videoinfo'=>$row['videoinfo']
            );
            echo json_encode($item).PHP_EOL;
        }
    }
}
?>