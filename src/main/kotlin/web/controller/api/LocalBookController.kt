package web.controller.api

import book.model.Book
import book.webBook.localBook.LocalBook
import org.apache.ibatis.solon.annotation.Db
import org.noear.solon.annotation.Controller
import org.noear.solon.annotation.Inject
import org.noear.solon.annotation.Mapping
import org.noear.solon.core.handle.UploadedFile
import org.noear.solon.core.util.DataThrowable
import org.noear.solon.web.cors.annotation.CrossOrigin
import web.mapper.BooklistMapper
import web.model.Booklist
import web.response.*
import web.util.cache.getlocalpath
import java.io.File
import java.net.URLDecoder
import kotlin.concurrent.thread

@Controller
@Mapping(routepath)
@CrossOrigin(origins = "*")
open class LocalBookController:BaseController() {

    @Db("db")
    @Inject
    lateinit var booklistMapper: BooklistMapper


    @Mapping("/importBookPreview")
    open fun importBookPreview(accessToken:String?, file: UploadedFile?)=run{
        if(file == null) throw DataThrowable().data(JsonResponse(false, NOT_BANK))
        if (file.isEmpty()) {
            throw DataThrowable().data(JsonResponse(false, NOT_BANK))
        }
        val user=getuserbytocken(accessToken).also {
            if(it == null){
                throw DataThrowable().data(JsonResponse(false, NEED_LOGIN))
            }
        }!!
        if(!(user.AllowUpTxt?:false)) {
            throw DataThrowable().data(JsonResponse(false,NOT_ALLOW_TXT))
        }
        if(!file.name.endsWith(".txt") && !file.name.endsWith(".epub")){
            throw DataThrowable().data(JsonResponse(false,NOT_TXT))
        }
        var f1=file.name
        kotlin.runCatching {
            f1= URLDecoder.decode( f1, "UTF-8" )
        }
        var  uploadDir = getlocalpath(user.username?:"")
        var ufile= File(uploadDir)
        if(!ufile.exists()){ufile.mkdirs()}
        var localpath=uploadDir+"/" + f1
        var  uploadedFile =  File(localpath)
        uploadedFile.writeBytes(file.contentAsBytes)
        val book = Book.initLocalBook(localpath, localpath, "")
        val chapters = LocalBook.getChapterList(book)
        if(booklistMapper.getbook(user.id!!,book.bookUrl) == null){
            val booklist= Booklist().create().bookto(book)
            booklist.originName="本地"
            booklist.userid=user.id
            booklist.lastCheckTime=System.currentTimeMillis()
            booklist.lastCheckCount=chapters.size
            booklist.totalChapterNum=chapters.size
            booklist.latestChapterTitle=chapters[chapters.size-1].title
            booklist.latestChapterTime=System.currentTimeMillis()
            booklistMapper.insert(booklist)
        }
        thread {
            ReadController.removeChapterListbycache(book.bookUrl,user.id!!)
            ReadController.removeallBookContentbycache(book.bookUrl,user.id!!)
            ReadController.setChapterListbycache(book.bookUrl,chapters,user.id!!)
        }
        return@run JsonResponse(true,SUCCESS).Data(mapOf("books" to book,"chapters" to chapters))
    }

}