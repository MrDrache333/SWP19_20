Softwareprojekt 2019/2020	- Dominion SP & MP
===============

Das Ziel des Softwareprojektes war es, eine digitale Spielversion des Brettspiels "Dominion" mittels agiler Methoden zu entwickeln. Die Besonderheit in dieser Iteration des Softwareprojektes war, dass wir ein Grundgerüst gegeben hatten und auf Basis eben dieses das Spiel entwickeln mussten.

[Link zu der Dokumentation](https://github.com/MrDrache333/Dominion-Java-Version-Server-Client/blob/master/SWP_Gruppe_B-komprimiert.pdf)

<img src="https://github.com/MrDrache333/Dominion-Java-Version-Server-Client/blob/master/gameview.PNG?raw=true">



Maven
-----
Das Projekt kann mit Maven gebaut werden. 

Dazu auf der Hauptebene zunächst

<pre><code>mvn clean install
</code></pre>

aufrufen.

Um den Client zu bauen:
<pre><code>cd client
mvn clean package
</code></pre>

der Client ist dann unter traget zu finden und kann dann z.B. wie folgt aufgerufen werden:

<pre><code>cd target
java -jar client-1.0-SNAPSHOT-jar-with-dependencies.jar
</code></pre>

Analoges gilt für den Server.
