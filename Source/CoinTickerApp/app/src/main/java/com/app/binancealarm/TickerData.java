package com.app.binancealarm;

public class TickerData {

    class HighKoinimTicker{
        double enYuksekfiyat;
        double enYuksek;
        boolean flag;
        public HighKoinimTicker(){
            flag = false;
            enYuksekfiyat = 0.0;
            enYuksek = 0.0;
        }
    }

    class LowKoinimTicker{
        double enDusukFiyat;
        double enDusuk;
        boolean flag;
        public LowKoinimTicker(){
            flag = false;
            enDusukFiyat = 0.0;
            enDusuk = 0.0;
        }
    }

    private TickerBinance ticker;
    private HighKoinimTicker high;
    private LowKoinimTicker low;

    public TickerData(TickerBinance ticker){
        this.ticker = ticker;
        this.high = new HighKoinimTicker();
        this.low = new LowKoinimTicker();
    }

    public TickerBinance getTicker() {
        return ticker;
    }

    public void setTicker(TickerBinance ticker) {
        this.ticker = ticker;
    }

    public HighKoinimTicker getHigh() {
        return high;
    }

    public LowKoinimTicker getLow() {
        return low;
    }
}
