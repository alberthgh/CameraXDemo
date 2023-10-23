package com.albert.cameraxdemo.misc

import android.content.Context
import java.io.File
import java.io.FileOutputStream

class FileUtil {
    companion object {
        private const val DEFAULT_PATH = "default"

        fun createFileOnDefaultPath(context: Context, subPath: String) {
            // 获取当前包名的files路径：/data/user/0/com.exa.myapplication
            //"data/data/com.exa.myapplication/"
            val PATH = context.dataDir.absolutePath + "/" + DEFAULT_PATH
            val file = File(PATH)
            if (!file.exists()) {
                if (!file.mkdirs()) {
//                    Log.e(TAG, "create directory failed.")
                }
            }
            File(PATH + "/" + subPath).createNewFile()
        }

        fun createDefaultDir(context: Context) {
            // 获取当前包名的files路径：/data/user/0/com.exa.myapplication
            //"data/data/com.exa.myapplication/"
            val PATH = context.dataDir.absolutePath + "/" + DEFAULT_PATH
            val file = File(PATH)
            if (!file.exists()) {
                if (!file.mkdirs()) {
//                    Log.e(TAG, "create directory failed.")
                }
            }
        }

        //不带"/"开头
        fun getDefaultPath(context: Context): String {
            return context.dataDir.absolutePath + "/" + DEFAULT_PATH
        }

        /**
         * @param subPath 不带"/"开头
         */
        private fun createFileAndPath(context: Context, subPath: String) {
            // 获取当前包名的files路径：/data/user/0/com.exa.myapplication/files
            val PATH = context.dataDir.absolutePath
            // 创建src和dst文件夹
            // 【注】需要有PATH目录的权限才能创建子目录
            // 若PATH文件夹权限为root权限，则使用adb shell chown命令修改权限
//            val src = File(PATH + "/" + "src")
            val src = File(PATH + subPath)
            // 判断文件夹是否存在，不存在就进行创建
            if (!src.exists()) {
                if (!src.mkdirs()) {
//                    Log.e(TAG, "create directory failed.")
                }
            }
            val dst = File(PATH + "/" + "dst")
            if (!dst.exists()) {
                if (!dst.mkdirs()) {
//                    Log.e(TAG, "create directory failed.")
                }
            }

            //！！不要 files/ 了。。。。
            // 创建info.txt文件，并写入数据———"hello info"
            val srcPath = File("data/data/com.exa.myapplication/files/src/info.txt")
            val fos = FileOutputStream(srcPath)
            fos.write("hello info".toByteArray())
            fos.close()

        }
    }
}