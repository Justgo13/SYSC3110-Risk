import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class RiskView extends JPanel {
    private HashMap<String, JButton> countryButtons;
    public RiskView() {
        countryButtons = new HashMap<>();
        setLayout(new GridBagLayout());
        GridBagConstraints mapConstraints = new GridBagConstraints();
        mapConstraints.weighty = 1.0;
        mapConstraints.weightx = 1.0;
        mapConstraints.anchor = GridBagConstraints.FIRST_LINE_START;

        /*
        NORTH AMERICA
         */

        // Alaska
        mapConstraints.fill = GridBagConstraints.BOTH;
        mapConstraints.insets = new Insets(0,50,0,0);
        mapConstraints.gridx = 0;
        mapConstraints.gridy = 0;
        JButton alaska = new JButton("Alaska");
        alaska.setName("Alaska");
        countryButtons.put(alaska.getName(), alaska);
        alaska.setEnabled(false);
        add(alaska, mapConstraints);

        // Northwest Canada
        mapConstraints.insets = new Insets(0,0,0,0);
        mapConstraints.gridx = 1;
        mapConstraints.gridy = 0;
        mapConstraints.gridwidth = 2;
        JButton nwCanada = new JButton("Northwest Territory");
        nwCanada.setName("Northwest Territory");
        countryButtons.put(nwCanada.getName(), nwCanada);
        nwCanada.setEnabled(false);
        add(nwCanada, mapConstraints);

        // Alberta
        mapConstraints.gridx = 1;
        mapConstraints.gridy = 1;
        mapConstraints.gridwidth = 1;
        JButton alberta = new JButton("Alberta");
        alberta.setName("Alberta");
        countryButtons.put(alberta.getName(), alberta);
        alberta.setEnabled(false);
        add(alberta, mapConstraints);

        // Ontario
        mapConstraints.gridx = 2;
        mapConstraints.gridy = 1;
        JButton ontario = new JButton("Ontario");
        ontario.setName("Ontario");
        countryButtons.put(ontario.getName(), ontario);
        ontario.setEnabled(false);
        add(ontario, mapConstraints);

        // Quebec
        mapConstraints.insets = new Insets(0,0,0,50);
        mapConstraints.gridx = 3;
        mapConstraints.gridy = 1;
        JButton quebec = new JButton("Quebec");
        quebec.setName("Quebec");
        countryButtons.put(quebec.getName(), quebec);
        quebec.setEnabled(false);
        add(quebec, mapConstraints);

        // West US
        mapConstraints.insets = new Insets(0,0,0,0);
        mapConstraints.gridx = 1;
        mapConstraints.gridy = 2;
        mapConstraints.gridheight = 2;
        JButton wUS = new JButton("Western United States");
        wUS.setName("Western United States");
        countryButtons.put(wUS.getName(), wUS);
        wUS.setEnabled(false);
        add(wUS, mapConstraints);

        // East US
        mapConstraints.gridx = 2;
        mapConstraints.gridy = 2;
        JButton eUS = new JButton("Eastern United States");
        eUS.setName("Eastern United States");
        countryButtons.put(eUS.getName(), eUS);
        eUS.setEnabled(false);
        add(eUS, mapConstraints);

        // Central America
        mapConstraints.gridx = 1;
        mapConstraints.gridy = 4;
        mapConstraints.gridheight = 1;
        JButton cAm = new JButton("Central America");
        cAm.setName("Central America");
        countryButtons.put(cAm.getName(), cAm);
        cAm.setEnabled(false);
        add(cAm, mapConstraints);

        // Greenland
        mapConstraints.insets = new Insets(0,0,0,50);
        mapConstraints.gridheight = 2;
        mapConstraints.gridx = 4;
        mapConstraints.gridy = 0;
        JButton greenland = new JButton("Greenland");
        greenland.setName("Greenland");
        countryButtons.put(greenland.getName(), greenland);
        greenland.setEnabled(false);
        add(greenland, mapConstraints);


        /*
        SOUTH AMERICA
         */

        // Venezuela
        mapConstraints.insets = new Insets(0,0,0,0);
        mapConstraints.gridheight = 1;
        mapConstraints.gridx = 2;
        mapConstraints.gridy = 5;
        JButton venezuela = new JButton("Venezuela");
        venezuela.setName("Venezuela");
        countryButtons.put(venezuela.getName(), venezuela);
        venezuela.setEnabled(false);
        add(venezuela, mapConstraints);

        // Peru
        mapConstraints.gridx = 2;
        mapConstraints.gridy = 6;
        JButton peru = new JButton("Peru");
        peru.setName("Peru");
        countryButtons.put(peru.getName(), peru);
        peru.setEnabled(false);
        add(peru, mapConstraints);

        // Argentina
        mapConstraints.gridheight = 2;
        mapConstraints.gridx = 2;
        mapConstraints.gridy = 7;
        JButton argentina = new JButton("Argentina");
        argentina.setName("Argentina");
        countryButtons.put(argentina.getName(), argentina);
        argentina.setEnabled(false);
        add(argentina, mapConstraints);

        // Brazil
        mapConstraints.gridx = 3;
        mapConstraints.gridy = 5;
        JButton brazil = new JButton("Brazil");
        brazil.setName("Brazil");
        countryButtons.put(brazil.getName(), brazil);
        brazil.setEnabled(false);
        add(brazil, mapConstraints);

        /*
        EUROPE
         */

        // Iceland
        mapConstraints.gridheight = 1;
        mapConstraints.gridwidth = 1;
        mapConstraints.gridx = 5;
        mapConstraints.gridy = 0;
        JButton iceland = new JButton("Iceland");
        iceland.setName("Iceland");
        countryButtons.put(iceland.getName(), iceland);
        iceland.setEnabled(false);
        add(iceland, mapConstraints);

        // Great Britain
        mapConstraints.insets = new Insets(0,50,50,50);
        mapConstraints.gridx = 6;
        mapConstraints.gridy = 1;
        JButton gBrit = new JButton("Great Britain");
        gBrit.setName("Great Britain");
        countryButtons.put(gBrit.getName(), gBrit);
        gBrit.setEnabled(false);
        add(gBrit, mapConstraints);

        // Western Europe
        mapConstraints.insets = new Insets(0,0,50,0);
        mapConstraints.gridx = 6;
        mapConstraints.gridy = 2;
        JButton wEur = new JButton("Western Europe");
        wEur.setName("Western Europe");
        countryButtons.put(wEur.getName(), wEur);
        wEur.setEnabled(false);
        add(wEur, mapConstraints);

        // South Europe
        mapConstraints.gridx = 7;
        mapConstraints.gridy = 2;
        JButton sEur = new JButton("Southern Europe");
        sEur.setName("Southern Europe");
        countryButtons.put(sEur.getName(), sEur);
        sEur.setEnabled(false);
        add(sEur, mapConstraints);

        // North Europe
        mapConstraints.insets = new Insets(0,0,0,0);
        mapConstraints.gridx = 7;
        mapConstraints.gridy = 1;
        JButton nEur = new JButton("Northern Europe");
        nEur.setName("Northern Europe");
        countryButtons.put(nEur.getName(), nEur);
        nEur.setEnabled(false);
        add(nEur, mapConstraints);

        // Scandinavia
        mapConstraints.gridx = 7;
        mapConstraints.gridy = 0;
        JButton scandinavia = new JButton("Scandinavia");
        scandinavia.setName("Scandinavia");
        countryButtons.put(scandinavia.getName(), scandinavia);
        scandinavia.setEnabled(false);
        add(scandinavia, mapConstraints);

        // Ukraine
        mapConstraints.gridwidth = 2;
        mapConstraints.gridheight = 3;
        mapConstraints.gridx = 8;
        mapConstraints.gridy = 0;
        JButton ukraine = new JButton("Ukraine");
        ukraine.setName("Ukraine");
        countryButtons.put(ukraine.getName(), ukraine);
        ukraine.setEnabled(false);
        add(ukraine, mapConstraints);

        /*
        ASIA
         */

        // Middle East
        mapConstraints.insets = new Insets(0,25,0,0);
        mapConstraints.gridwidth = 2;
        mapConstraints.gridheight = 3;
        mapConstraints.gridx = 8;
        mapConstraints.gridy = 3;
        JButton middleEast = new JButton("Middle East");
        middleEast.setName("Middle East");
        countryButtons.put(middleEast.getName(), middleEast);
        middleEast.setEnabled(false);
        add(middleEast, mapConstraints);
        mapConstraints.insets = new Insets(0,0,0,0);

        // Ural
        mapConstraints.gridwidth = 1;
        mapConstraints.gridheight = 2;
        mapConstraints.gridx = 10;
        mapConstraints.gridy = 0;
        JButton ural = new JButton("Ural");
        ural.setName("Ural");
        countryButtons.put(ural.getName(), ural);
        ural.setEnabled(false);
        add(ural, mapConstraints);

        // Afghanistan
        mapConstraints.gridheight = 1;
        mapConstraints.gridx = 10;
        mapConstraints.gridy = 2;
        JButton afghan = new JButton("Afghanistan");
        afghan.setName("Afghanistan");
        countryButtons.put(afghan.getName(), afghan);
        afghan.setEnabled(false);
        add(afghan, mapConstraints);

        // India
        mapConstraints.gridheight = 2;
        mapConstraints.gridwidth = 1;
        mapConstraints.gridx = 10;
        mapConstraints.gridy = 3;
        JButton india = new JButton("India");
        india.setName("India");
        countryButtons.put(india.getName(), india);
        india.setEnabled(false);
        add(india, mapConstraints);

        // Siberia
        mapConstraints.gridwidth = 1;
        mapConstraints.gridheight = 2;
        mapConstraints.gridx = 11;
        mapConstraints.gridy = 0;
        JButton siberia = new JButton("Siberia");
        siberia.setName("Siberia");
        countryButtons.put(siberia.getName(), siberia);
        siberia.setEnabled(false);
        add(siberia, mapConstraints);

        // China
        mapConstraints.gridheight = 2;
        mapConstraints.gridwidth = 2;
        mapConstraints.gridx = 11;
        mapConstraints.gridy = 3;
        JButton china = new JButton("China");
        china.setName("China");
        countryButtons.put(china.getName(), china);
        china.setEnabled(false);
        add(china, mapConstraints);

        // Yakutsk
        mapConstraints.gridwidth = 1;
        mapConstraints.gridheight = 1;
        mapConstraints.gridx = 12;
        mapConstraints.gridy = 0;
        JButton yakutsk = new JButton("Yakutsk");
        yakutsk.setName("Yakutsk");
        countryButtons.put(yakutsk.getName(), yakutsk);
        yakutsk.setEnabled(false);
        add(yakutsk, mapConstraints);

        // Irkutsk
        mapConstraints.gridx = 12;
        mapConstraints.gridy = 1;
        JButton irkutsk = new JButton("Irkutsk");
        irkutsk.setName("Irkutsk");
        countryButtons.put(irkutsk.getName(), irkutsk);
        irkutsk.setEnabled(false);
        add(irkutsk, mapConstraints);

        // Mongolia
        mapConstraints.gridx = 12;
        mapConstraints.gridy = 2;
        JButton mongolia = new JButton("Mongolia");
        mongolia.setName("Mongolia");
        countryButtons.put(mongolia.getName(), mongolia);
        mongolia.setEnabled(false);
        add(mongolia, mapConstraints);

        // Kamchatka
        mapConstraints.gridheight = 3;
        mapConstraints.gridx = 13;
        mapConstraints.gridy = 0;
        JButton kamchatka = new JButton("Kamchatka");
        kamchatka.setName("Kamchatka");
        countryButtons.put(kamchatka.getName(), kamchatka);
        kamchatka.setEnabled(false);
        add(kamchatka, mapConstraints);

        // Japan
        mapConstraints.insets = new Insets(0,50,0,0);
        mapConstraints.gridwidth = 1;
        mapConstraints.gridheight = 1;
        mapConstraints.gridx = 14;
        mapConstraints.gridy = 3;
        JButton japan = new JButton("Japan");
        japan.setName("Japan");
        countryButtons.put(japan.getName(), japan);
        japan.setEnabled(false);
        add(japan, mapConstraints);
        mapConstraints.insets = new Insets(0,0,0,0);

        // Siam
        mapConstraints.gridx = 11;
        mapConstraints.gridy = 5;
        JButton siam = new JButton("Siam");
        siam.setName("Siam");
        countryButtons.put(siam.getName(), siam);
        siam.setEnabled(false);
        add(siam, mapConstraints);

        /*
        AUSTRALIA
         */

        // Indonesia
        mapConstraints.gridx = 12;
        mapConstraints.gridy = 6;
        JButton indonesia = new JButton("Indonesia");
        indonesia.setName("Indonesia");
        countryButtons.put(indonesia.getName(), indonesia);
        indonesia.setEnabled(false);
        add(indonesia, mapConstraints);

        // New Guinea
        mapConstraints.insets = new Insets(0,50,0,0);
        mapConstraints.gridx = 13;
        mapConstraints.gridy = 6;
        JButton newGuinea = new JButton("New Guinea");
        newGuinea.setName("New Guinea");
        countryButtons.put(newGuinea.getName(), newGuinea);
        newGuinea.setEnabled(false);
        add(newGuinea, mapConstraints);
        mapConstraints.insets = new Insets(0,0,0,0);

        // West Australia
        mapConstraints.insets = new Insets(50,0,0,0);
        mapConstraints.gridx = 12;
        mapConstraints.gridy = 7;
        JButton wAus = new JButton("Western Australia");
        wAus.setName("Western Australia");
        countryButtons.put(wAus.getName(), wAus);
        wAus.setEnabled(false);
        add(wAus, mapConstraints);

        // East Australia
        mapConstraints.insets = new Insets(50,0,0,0);
        mapConstraints.gridx = 13;
        mapConstraints.gridy = 7;
        JButton eAus = new JButton("Eastern Australia");
        eAus.setName("Eastern Australia");
        countryButtons.put(eAus.getName(), eAus);
        eAus.setEnabled(false);
        add(eAus, mapConstraints);
        mapConstraints.insets = new Insets(0,0,0,0);

        /*
        AFRICA
         */

        // North Africa
        mapConstraints.gridheight = 2;
        mapConstraints.gridwidth = 2;
        mapConstraints.gridx = 5;
        mapConstraints.gridy = 3;
        JButton nAfr = new JButton("North Africa");
        nAfr.setName("North Africa");
        countryButtons.put(nAfr.getName(), nAfr);
        nAfr.setEnabled(false);
        add(nAfr, mapConstraints);

        // Congo
        mapConstraints.gridheight = 1;
        mapConstraints.gridwidth = 1;
        mapConstraints.gridx = 6;
        mapConstraints.gridy = 5;
        JButton congo = new JButton("Congo");
        congo.setName("Congo");
        countryButtons.put(congo.getName(), congo);
        congo.setEnabled(false);
        add(congo, mapConstraints);

        // South Africa
        mapConstraints.gridheight = 2;
        mapConstraints.gridwidth = 2;
        mapConstraints.gridx = 6;
        mapConstraints.gridy = 6;
        JButton sAfr = new JButton("South Africa");
        sAfr.setName("South Africa");
        countryButtons.put(sAfr.getName(), sAfr);
        sAfr.setEnabled(false);
        add(sAfr, mapConstraints);

        // Egypt
        mapConstraints.gridheight = 1;
        mapConstraints.gridwidth = 1;
        mapConstraints.gridx = 7;
        mapConstraints.gridy = 3;
        JButton egypt = new JButton("Egypt");
        egypt.setName("Egypt");
        countryButtons.put(egypt.getName(), egypt);
        egypt.setEnabled(false);
        add(egypt, mapConstraints);

        // East Africa
        mapConstraints.gridheight = 2;
        mapConstraints.gridwidth = 1;
        mapConstraints.gridx = 7;
        mapConstraints.gridy = 4;
        JButton eAfr = new JButton("East Africa");
        eAfr.setName("East Africa");
        countryButtons.put(eAfr.getName(), eAfr);
        eAfr.setEnabled(false);
        add(eAfr, mapConstraints);

        // Madagascar
        mapConstraints.insets = new Insets(0,50,0,0);
        mapConstraints.gridheight = 1;
        mapConstraints.gridwidth = 1;
        mapConstraints.gridx = 8;
        mapConstraints.gridy = 7;
        JButton madagascar = new JButton("Madagascar");
        madagascar.setName("Madagascar");
        countryButtons.put(madagascar.getName(), madagascar);
        madagascar.setEnabled(false);
        add(madagascar, mapConstraints);
        mapConstraints.insets = new Insets(0,0,0,0);
    }
}
