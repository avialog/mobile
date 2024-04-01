class LoggerDebug : ILogger {
    override fun w(throwable: Throwable) {
        throwable.printStackTrace()
        // TODO
    }
}
