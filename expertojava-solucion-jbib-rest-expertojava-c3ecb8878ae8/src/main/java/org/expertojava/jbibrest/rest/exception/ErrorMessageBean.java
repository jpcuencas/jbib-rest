package org.expertojava.jbibrest.rest.exception;


import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorMessageBean {

        // HTTP Status code devuelto por el servidor en forma de String
        //p.ej. si es "404 Not found", tiene el valor "Not found"
        @XmlElement(name = "status")
        String status;

        // valor numérico del HTTP Status code
        @XmlElement(name = "code")
        int code;

        // descripción del error
        @XmlElement(name = "message")
        String message;

        // información extra proporcionada por los desarrolladores
        @XmlElement(name = "developerMessage")
        String developerMessage;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDeveloperMessage() {
            return developerMessage;
        }

        public void setDeveloperMessage(String developerMessage) {
            this.developerMessage = developerMessage;
        }

        public ErrorMessageBean(Response.Status status, String mensaje1, String mensaje2){
            this.code = status.getStatusCode();
            this.status = status.getReasonPhrase();
            this.message = mensaje1;
            this.developerMessage = mensaje2;
        }
        public ErrorMessageBean() {}
}
