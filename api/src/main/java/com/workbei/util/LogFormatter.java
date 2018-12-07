package com.workbei.util;

/**
 * @author Wallace Mao
 * Date: 2018-11-22 15:50
 */
public class LogFormatter {

    public static enum LogEvent {
        EXCEPTION("EXCEPTION"),
        COMMON("COMMON"),
        START("START"),
        END("END");

        private String value;

        LogEvent(String value){
            this.value = value;
        }
        public String getValue(){
            return this.value;
        }
    }

    public static class KeyValue{
        private String key;
        private Object value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public KeyValue(String key, Object value){
            this.key = key;
            this.value = value;
        }
    }

    private static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    public static String format(LogEvent logEvent, KeyValue... args){
        return format(logEvent, null, args);
    }

    public static String format(LogEvent logEvent, String message, KeyValue... args){
        StringBuilder logData = new StringBuilder();

        if(!isEmpty(logEvent)){
            logData.append("logEvent: ");
            logData.append(logEvent);
        }
        if(!isEmpty(message)){
            logData.append("\t");
            logData.append("msg: ");
            logData.append(message);
        }
        if(args.length > 0){
            for(int i = 0; i < args.length; i++){
                KeyValue kv = args[i];
                if(kv != null){
                    logData.append("\t");
                    logData.append(kv.getKey());
                    logData.append(": ");
                    logData.append(kv.getValue());
                }
            }
        }

        return logData.toString();
    }

    public static KeyValue getKV(String key, Object value){
        return new KeyValue(key, value);
    }
}
