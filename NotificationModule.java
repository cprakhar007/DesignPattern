import java.util.*;

// Observer / PubSub pattern
interface Observer {
    void update(String subject, String message);
}

class User implements Observer {
    private final String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void update(String subject, String message) {
        System.out.println(name + " received a notification:");
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println();
    }
}

class NotificationService {
    private static NotificationService instance;

    private final Map<String, List<Observer>> subscribers;

    private NotificationService() {
        subscribers = new HashMap<>();
    }

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    public void subscribe(String channel, Observer observer) {
        subscribers.computeIfAbsent(channel, k -> new ArrayList<>()).add(observer);
    }

    public void sendNotification(String channel, String subject, String message) {
        List<Observer> observers = subscribers.getOrDefault(channel, new ArrayList<>());
        for (Observer observer : observers) {
            observer.update(subject, message);
        }
    }
}

// Factory pattern
interface NotificationChannelFactory {
    NotificationChannel createChannel();
}

class EmailChannelFactory implements NotificationChannelFactory {
    @Override
    public NotificationChannel createChannel() {
        return new EmailChannel();
    }
}

class SMSChannelFactory implements NotificationChannelFactory {
    @Override
    public NotificationChannel createChannel() {
        return new SMSChannel();
    }
}

// Strategy pattern
interface NotificationChannel {
    void sendNotification(String subject, String message);
}

class EmailChannel implements NotificationChannel {
    @Override
    public void sendNotification(String subject, String message) {
        System.out.println("Sending email notification:");
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println();
    }
}

class SMSChannel implements NotificationChannel {
    @Override
    public void sendNotification(String subject, String message) {
        System.out.println("Sending SMS notification:");
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println();
    }
}

// Facade pattern
class NotificationFacade {
    private final NotificationChannelFactory emailFactory;
    private final NotificationChannelFactory smsFactory;
    private final NotificationService notificationService;

    public NotificationFacade() {
        emailFactory = new EmailChannelFactory();
        smsFactory = new SMSChannelFactory();
        notificationService = NotificationService.getInstance();
    }

    public void sendNotification(String channelType, String subject, String message) {
        NotificationChannelFactory factory;
        if ("Email".equalsIgnoreCase(channelType)) {
            factory = emailFactory;
        } else if ("SMS".equalsIgnoreCase(channelType)) {
            factory = smsFactory;
        } else {
            throw new IllegalArgumentException("Invalid channel type");
        }

        NotificationChannel channel = factory.createChannel();
        channel.sendNotification(subject, message);
    }

    public void subscribe(String channelType, Observer observer) {
        notificationService.subscribe(channelType, observer);
    }
}

public class Main {
    public static void main(String[] args) {
        NotificationFacade notificationFacade = new NotificationFacade();

        // Mocked users
        Observer user1 = new User("User1");
        Observer user2 = new User("User2");

        notificationFacade.subscribe("Email", user1);
        notificationFacade.subscribe("SMS", user2);

        String channel = args[0];  // Provide channel argument: "Email" or "SMS"
        String subject = args[1];  // Provide subject argument
        String message = args[2];  // Provide message argument

        notificationFacade.sendNotification(channel, subject, message);
    }
}
