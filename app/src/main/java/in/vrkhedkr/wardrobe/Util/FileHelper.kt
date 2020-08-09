import android.content.Context
import java.io.File
import java.util.*

object FileHelper {

    fun getFile(context: Context):File{
        val rootDirectory = context.filesDir
        val imageDirectory = File(rootDirectory,".images")
        if(!imageDirectory.exists()){
            imageDirectory.mkdir()
        }
        return File(imageDirectory, "IMG_" + Date().time)
    }

}
