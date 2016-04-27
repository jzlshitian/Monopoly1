package game;

import object.*;

import java.util.*;
import java.text.*;

/**
 * Created by jzl on 16/4/2.
 */
public class Game {
    private static final String NAME_INPUT = "请输入玩家%d的名字:\n",
        GAME_START = "游戏开始\n";
    private static final int MAX_PLAYER = 2,
        MIN_STOCK = 10;
    private static final String[] STOCK_NAME =
            {"Nike", "Oracle", "Apple", "Citi", "Fort",
                    "Boeing", "Toyota", "Intel", "Yahoo", "Cisco"};

    private ArrayList<Player> players;
    private int currentPlayer;
    private Calendar calendar;
    private Menu menu;
    private Map map;
    private Stock[] stocks;

    public Game() {
        players = new ArrayList<>();
        for (int i=0;i<MAX_PLAYER;i++)
            players.add(new Player(i));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日");
        calendar = Calendar.getInstance();
        try {
            Date date = simpleDateFormat.parse("2014年1月1日");
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        menu = new Menu();
        map = new Map();

        stocks = new Stock[MIN_STOCK];
        for (int i=0;i<MIN_STOCK;i++)
            stocks[i] = new Stock(STOCK_NAME[i], i);
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public void tomorrow() {
        calendar.add(Calendar.DATE, 1);
        for (int i=0;i<stocks.length;i++)
            stocks[i].tomorrow();
    }

    public void setPlayerNames() {
        for (int i=0;i<players.size();i++) {
            System.out.printf(NAME_INPUT, i+1);
            Scanner sc = new Scanner(System.in);
            String name = sc.next();
            players.get(i).setName(name);
        }
    }

    public void startGame() {
        buildMap();
        System.out.print(GAME_START);

        while (players.size()!=1) {
            for (int i=0;i<players.size();i++) {
                int option = -1;
                while (option != 7 && option != 6)
                    option = menu.printMainMenu(stocks, map, calendar, players, currentPlayer);
                nextPlayer(option);
            }
            tomorrow();
        }
    }

    private void buildMap() {
        int landNameRow = 0, landNameColumn = 0;

        for (int y=0;y<Map.MAP_HEIGHT;y++) {
            for (int x=0;x<Map.MAP_WIDTH;x++) {
                if (Map.INITIAL_MAP[y][x] == '\u3000')
                    continue;
                Cell curCell = this.map.createCell(x, y);
                switch(Map.INITIAL_MAP[y][x]) {
                    case '◎':
                        if (landNameColumn == Map.LAND_NAME[landNameRow].length) {
                            landNameColumn = 0;
                            landNameRow++;
                        }
                        Land land = new Land(landNameRow, Map.LAND_NAME[landNameRow][landNameColumn++]);
                        curCell.addView(land);
                        curCell.setServing(land);
                        break;
                    case '新':
                        NewsCentre newsCentre = new NewsCentre();
                        curCell.addView(newsCentre);
                        curCell.setServing(newsCentre);
                        break;
                    case '银':
                        Bank bank = new Bank();
                        curCell.addView(bank);
                        curCell.setServing(bank);
                        break;
                    case '道':
                        ItemShop itemShop = new ItemShop();
                        curCell.addView(itemShop);
                        curCell.setServing(itemShop);
                        break;
                    case '券':
                        PointGetter pointGetter = new PointGetter();
                        curCell.addView(pointGetter);
                        curCell.setServing(pointGetter);
                        break;
                    case '空':
                        Opening opening = new Opening();
                        curCell.addView(opening);
                        curCell.setServing(opening);
                        break;
                    case '卡':
                        ItemGetter itemGetter = new ItemGetter();
                        curCell.addView(itemGetter);
                        curCell.setServing(itemGetter);
                        break;
                    case '彩':
                        LotteryHouse lotteryHouse = new LotteryHouse();
                        curCell.addView(lotteryHouse);
                        curCell.setServing(lotteryHouse);
                        break;

                }
            }
        }
        for (int i=0;i<players.size();i++)
            map.getCell(0, 0).addView(players.get(i));
    }

    private void nextPlayer(int option) {
        if (option == 6)
            currentPlayer = (currentPlayer + 1) % players.size();
        else if (option == 7)
            currentPlayer %= players.size();
    }
}
