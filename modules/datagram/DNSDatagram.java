package modules.datagram;

public class DNSDatagram extends DatagramBuilder {
    private Header header = null;
    private Request request = null;
    private Response response = null;

    public DNSDatagram (Header header, Request request) {
        this.header = header;
        this.request = request;
    }

    public DNSDatagram (Header header, Request request, Response response) {
        this.header = header;
        this.request = request;
        this.response = response;
    }

    public Header getHeader() {
        return header;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    public byte[] build() {
        if (this.response!=null)
            return bytesConcat(bytesConcat(this.header.build(), this.request.build()),this.response.build());
        else
            return bytesConcat(this.header.build(), this.request.build());
    }

    public void debugOutput() {
        this.header.output();
        this.request.output();
        if (this.response!=null)
            this.response.output();
    }
}
