## SyntaxHighlight 程序
- 基于Swing
- 实现输入一个程序，将其转换为带高亮关键字的html文件
- 可以通过修改keywork.txt使用其他关键字

##测试
```java
package com.lpl;

import java.util.Queue;

class Consumer extends Thread {
    private final Queue sharedQueue;

    public Consumer(Queue sharedQueue) {
        super();
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (sharedQueue) {
                while (sharedQueue.size() == 0) {
                    try {
                        System.out.println("队列空了，等待生产");
                        sharedQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Object number = sharedQueue.poll();
                System.out.println("进行消费 : " + number);
                sharedQueue.notify();
            }
        }
    }
}

```

