package com.zzzl.kotlinbasic

/**
 * 同步方法
 */
class TicketWindow(private val windowName: String) : Thread() {

    var count = 0
    override fun run() {
        super.run()
        do {
            Thread.sleep(100)
            Ticket.sell(this)
        }while (Ticket.tickets > 0)
        println("窗口${windowName}结束，一共出售了 ${count} 张票")
    }

}

object Ticket {

    var tickets = 100

    // 添加 @Synchronized 注解用来表明这是一个同步方法
    @Synchronized
    fun sell(ticketWindow: TicketWindow) {
        if(tickets > 0){
            println("${ticketWindow.name} ：${tickets--}")
            ticketWindow.count++
        }else{
            println("票已经售罄")
        }
    }
}

fun main() {
    TicketWindow("窗口1").start()
    Thread.sleep(400)
    TicketWindow("窗口2").start()
    Thread.sleep(500)
    TicketWindow("窗口3").start()
}