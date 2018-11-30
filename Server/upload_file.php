<?php
/**
 * 保存视频文件         Save this video to location
 * 生成预览图           Make a cover for this video and save it
 * 获取视频信息         Get the deatils info of this video
 * 往数据库里添加纪录   Write Note into mysql databases
 */

include 'mysql_header.php';

$currentTime=time();
move_uploaded_file($_FILES["file"]["tmp_name"],"videos/".$currentTime);

$ffmpeg_path = 'ffmpeg'; //or: /usr/bin/ffmpeg , or /usr/local/bin/ffmpeg - depends on your installation (type which ffmpeg into a console to find the install path)
$vid = "videos/".$currentTime; //Replace here!

if (file_exists($vid)) {// 如果文件存在；If file is exists;

    $finfo = finfo_open(FILEINFO_MIME_TYPE);
    $mime_type = finfo_file($finfo, $vid); // check mime type
    finfo_close($finfo);

    if (preg_match('/video\/*/', $mime_type)) {

        $video_attributes = json_encode(_get_video_attributes($vid, $ffmpeg_path));
        getVideoCover($vid,1,$vid.".jpg");// 生成视频缩略图
        
        $sql="insert into videos (filename,videoname,count,videoinfo) values ({$currentTime}, '{$_FILES["file"]["name"]}', 0, '{$video_attributes}')";
        if ($conn->query($sql) === TRUE) {
            echo "文件上传成功";
        } else {
            echo "Error: " . $sql . "<br>" . $conn->error;
        }
        $conn->close();
    } else {// 不是视频文件，所以我们应该把它删除;File Is not a video, so we should delete it.
        unlink("videos/".$currentTime);// 
        print_r('File is not a video.');
    }
} else {
    print_r('File does not exist.');
}

function _get_video_attributes($video, $ffmpeg) {

    $command = $ffmpeg . ' -i ' . $video . ' -vstats 2>&1';
    $output = shell_exec($command);

    $regex_sizes = "/Video: ([^,]*), ([^,]*), ([0-9]{1,4})x([0-9]{1,4})/"; // or : $regex_sizes = "/Video: ([^\r\n]*), ([^,]*), ([0-9]{1,4})x([0-9]{1,4})/"; (code from @1owk3y)
    if (preg_match($regex_sizes, $output, $regs)) {
        $codec = $regs [1] ? $regs [1] : null;
        $width = $regs [3] ? $regs [3] : null;
        $height = $regs [4] ? $regs [4] : null;
    }

    $regex_duration = "/Duration: ([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2}).([0-9]{1,2})/";
    if (preg_match($regex_duration, $output, $regs)) {
        $hours = $regs [1] ? $regs [1] : null;
        $mins = $regs [2] ? $regs [2] : null;
        $secs = $regs [3] ? $regs [3] : null;
        $ms = $regs [4] ? $regs [4] : null;
    }

    return array('codec' => $codec,
        'width' => $width,
        'height' => $height,
        'time' => $hours.":".$mins.":".$secs.".".$ms,
        'size' => filesize($video)
    );
}

//生成视频文件的缩略图;Create the video cover
function getVideoCover($file,$time,$name) {
    if(empty($time))$time = '1';//默认截取第一秒第一帧;Get the 1th ffp at 1th second
    $strlen = strlen($file);
    $str = "ffmpeg -i ".$file." -y -f mjpeg -ss 3 -t ".$time." -s 320x240 ".$name;
    $result = system($str);
}
?>