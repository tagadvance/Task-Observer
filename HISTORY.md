# History

I started this project as a means to test the claims made by [CPUCores](http://store.steampowered.com/app/384300/). In particular, I wanted to know if tweaking the priority and/or affinity of a game would increase one's frames per second (FPS).

**TL;DR**
The Windows scheduler is best left alone. In nearly every scenario, you are better off closing extraneous processes than messing with affinity and priority. When tweaking the priority, there was, at best, very little discernible difference (.3 FPS, well within the margin of error). At worst, realtime priority performed much, much worse than normal priority. In my benchmark tests modifying the affinity of a process often caused more harm than good.

Hardware
---
**Platform:**	Windows 10 x64<br/>
**CPU model:**	AMD FX(tm)-8350 Eight-Core Processor (4334MHz)<br/>
**GPU model:**	NVIDIA GeForce GTX 1080<br/>

NovaBench (Integer Operations per Second)
===
In the following three tests, priority appears to have little or no affect. From this we can conclude that priority doesn't really come into effect until the CPU begins to run out of resources.
**Low Priority:**<br/>
644,990,520<br/>
647,550,960<br/>

**Normal Priority:**<br/>
644,470,960<br/>
649,479,264<br/>

**Realtime:**<br/>
649,768,344<br/>
648,146,528<br/>

In the following 4 tests, _yes_ the dedicated single core affinity performs better than the shared affinity; _however,_ the output is on par with the numbers from the above tests where the affinity wasn't set, i.e. the scheduler is actually pretty good at its job.
**Normal Priority Single Core Dedicated Affinity:**<br/>
648,648,184<br/>

**Normal Priority Single Core Shared Affinity:**<br/>
441,061,368<br/>
461,104,872<br/>

**Realtime Single Core Shared Affinity:**<br/>
422,479,352<br/>
428,741,880<br/>

**Low Priority Single Core Shared Affinity:**<br/>
426,373,288<br/>

For the tests below I forced everything on to two cores with NovaBench running on one of those two cores.<br/>
**Low Priority:**<br/>
177,674,952<br/>
149,457,080<br/>

**Below Normal Priority:**<br/>
183,248,112<br/>

**Normal priority:**<br/>
224,059,136<br/>
218,839,680<br/>

**Realtime priority:** _(WTF?)_<br/>
105,269,104<br/>
75,479,840<br/>

Unigine Heaven Benchmark 4.0
===
In a two core affinity configuration, I was getting 13-15 FPS. The sound was buffeting in and out which was very annoying.

In a dedicated 4 core affinity configuration, there were two main threads observed; however, my per core speed simply isn't fast enough to keep up. The load would occasionally jump to two other cores. When I flip and rotate a screenshot of my Task manager the valleys of the first two cores match up almost exactly with the peaks of the latter two cores. The sound would stutter and there were these obnoxious staticy popping sounds which was rather annoying.

**Low Priority:**<br/>
**FPS:**		37.6<br/>
**Score:**		946<br/>
**Min FPS:**	19.7<br/>
**Max FPS:**	91.5<br/>

**Baseline:**<br/>
**FPS:**		37.6<br/>
**Score:**		948<br/>
**Min FPS:**	20.7<br/>
**Max FPS:**	93.8<br/>

**Realtime:**<br/>
**FPS:**		37.8<br/>
**Score:**		952<br/>
**Min FPS:**	21.0<br/>
**Max FPS:**	94.7<br/>

**Shared 4 core affinity:**<br/>
**FPS:**		37.9<br/>
**Score:**		954<br/>
**Min FPS:**	21.0<br/>
**Max FPS:**	95.9<br/>

**Dedicated 4 core affinity Realtime:**<br/>
**FPS:**		34.0<br/>
**Score:**		856<br/>
**Min FPS:**	17.3<br/>
**Max FPS:**	86.2<br/>

In Conclusion
===
I **strongly** recommend that you let the scheduler do its job. Close extraneous processes (I'm looking at you, Chrome). Look, these results are far from scientific. Your mileage may vary.