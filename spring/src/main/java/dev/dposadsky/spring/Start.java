package dev.dposadsky.spring;

import dev.dposadsky.spring.performers.Performer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        Performer poeticJuggler = (Performer) applicationContext.getBean("dev.dposadsky.spring.performers.PoeticJuggler");
        poeticJuggler.perform();

        Performer instrumentalist = (Performer) applicationContext.getBean("instrumentalist");
        instrumentalist.perform();

        Performer oneManBand = (Performer) applicationContext.getBean("dev.dposadsky.spring.performers.OneManBand");
        oneManBand.perform();

        Performer oneManBandInfo = (Performer) applicationContext.getBean("dev.dposadsky.spring.performers.OneManBandInfo");
        oneManBandInfo.perform();

        Performer oneManBandProps = (Performer) applicationContext.getBean("dev.dposadsky.spring.performers.OneManBandProps");
        oneManBandProps.perform();

        Performer instrumentalistClone = (Performer) applicationContext.getBean("dev.dposadsky.spring.performers.InstrumentalistClone");
        instrumentalistClone.perform();

        Performer dancer = (Performer) applicationContext.getBean("dev.dposadsky.spring.performers.Dancer");
        dancer.perform();

    }

}
