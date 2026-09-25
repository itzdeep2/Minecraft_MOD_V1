# Minecraft Space Program

A custom Minecraft Fabric mod that replaces basic teleportation tricks with a launch system featuring real physics calculations, staged exhaust effects, an emergency parachute bail-out system, and procedural orbital space station generation.

## Description

I built this mod for Minecraft 1.20 because vanilla survival lacks realistic spaceflight mechanics. 

Instead of dummy command-block animations, this mod introduces a custom Launch Pad block that constructs and ignites a rocket entity with Newtonian tick-based physics. The flight model dynamically calculates acceleration from fuel burn rates, dry mass versus remaining propellant mass, and atmospheric drag based on altitude. It includes physical launch shudder vibrations, engine exhaust particles, and a transonic vapor cone shockwave when crossing high speeds.

To make survival exploration practical, boarding the vehicle automatically equips an Emergency Parachute into your pack. If you bail out mid-flight, deploying it arrests downward velocity with a cloud burst and applies slow-falling immunity. Once you climb to the world height limit ($Y = 320$), the vehicle automatically docks, dismounts the player, and procedurally generates an orbital research station complete with tinted observation windows, life support styling, beds, and survival food rations overlooking the world below.

### Screenshots

https://drive.google.com/file/d/1WQWpkT7SSfuaiHYjMZ-7b6_ci8R8KyoY/view?usp=drivesdk

## Getting Started

### Dependencies

* **OS**: Windows 10/11, macOS, or Linux
* **Java**: OpenJDK 17 or Java 21 installed and added to your system `PATH`
* **Minecraft**: Version 1.20 (runs via Fabric Loom development client)

### Installing

1. Clone the repository to your local computer:
```bash
git clone [https://github.com/itzdeep2/Minecraft-Mod-V1.git](https://github.com/itzdeep2/Minecraft-Mod-V1.git)
./gradlew runClient
