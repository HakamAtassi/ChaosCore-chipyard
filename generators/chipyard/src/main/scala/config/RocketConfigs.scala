package chipyard

import org.chipsalliance.cde.config.{Config}
import freechips.rocketchip.prci.{AsynchronousCrossing}
import freechips.rocketchip.subsystem.{InCluster}


import freechips.rocketchip.subsystem._

// --------------
// Rocket Configs
// --------------

class RV32RocketConfig extends Config(
  new freechips.rocketchip.rocket.WithRV32 ++            // set RocketTiles to be 32-bit
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++


  new freechips.rocketchip.subsystem.WithCustomMemPort(base_addr = BigInt("40000000", 16), 
                                                       base_size = BigInt("10000000", 16), 
                                                       data_width = 64, 
                                                       maxXferBytes = 16,
                                                       id_bits = 4) ++

  new ChaosCore.WithPPM() ++ 
  //new ChaosCore.WithSDL2() ++ 

  new testchipip.boot.WithBootAddrReg(testchipip.boot.BootAddrRegParams(
    defaultBootAddress = 0x40000000L, 
    bootRegAddress = 0x1000,
    slaveWhere = PBUS)
  ) ++


  new chipyard.config.AbstractConfig)


class RV32RocketFPGAConfig extends Config(
  new freechips.rocketchip.rocket.WithRV32 ++            // set RocketTiles to be 32-bit
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++ //With1TinyCore ++ 
  //new freechips.rocketchip.subsystem.WithoutTLMonitors ++
  //new chipyard.config.WithBroadcastManager ++ // no l2

  // set custom MMIO
  new chipyard.config.WithNoUART() ++       // Dont use SiFive UART
  new chipyard.config.WithNoDebug() ++      // Dont use top level JTAG Debug IO
  new testchipip.serdes.WithNoSerialTL ++   // With no output serial TL

  new chipyard.config.WithNoUART() ++       // Dont use SiFive UART
  new chipyard.config.WithNoDebug() ++      // Dont use top level JTAG Debug IO
  new testchipip.serdes.WithNoSerialTL ++   // With no output serial TL
  new freechips.rocketchip.subsystem.WithoutTLMonitors ++ 

  new freechips.rocketchip.subsystem.WithCustomMMIOPort (
    base_addr = BigInt("FF000000", 16),  // Use a string to ensure correct interpretation
    base_size = BigInt("00100000", 16),  // Ensure it's interpreted correctly
    data_width = 64,       
    maxXferBytes = 8,     
    id_bits = 4
  ) ++

  // set custom DRAM 
  new freechips.rocketchip.subsystem.WithCustomMemPort(base_addr = BigInt("40000000", 16), 
                                                       base_size = BigInt("10000000", 16), 
                                                       data_width = 64, 
                                                       maxXferBytes = 16,
                                                       id_bits = 4) ++


  new testchipip.boot.WithBootAddrReg(testchipip.boot.BootAddrRegParams(
    defaultBootAddress = 0x40000000L, // This should be DRAM_BASE
    bootRegAddress = 0x1000,
    slaveWhere = PBUS)
  ) ++


  new chipyard.config.AbstractConfig)


class RocketConfig extends Config(
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++         // single rocket-core
  new chipyard.config.AbstractConfig)


class DualRocketConfig extends Config(
  new freechips.rocketchip.rocket.WithNHugeCores(2) ++
  new chipyard.config.AbstractConfig)

class TinyRocketConfig extends Config(
  new chipyard.harness.WithDontTouchChipTopPorts(false) ++        // TODO FIX: Don't dontTouch the ports
  new testchipip.soc.WithNoScratchpads ++                         // All memory is the Rocket TCMs
  new freechips.rocketchip.subsystem.WithIncoherentBusTopology ++ // use incoherent bus topology
  new freechips.rocketchip.subsystem.WithNBanks(0) ++             // remove L2$
  new freechips.rocketchip.subsystem.WithNoMemPort ++             // remove backing memory
  new freechips.rocketchip.rocket.With1TinyCore ++                // single tiny rocket-core
  new chipyard.config.AbstractConfig)

class QuadRocketConfig extends Config(
  new freechips.rocketchip.rocket.WithNHugeCores(4) ++    // quad-core (4 RocketTiles)
  new chipyard.config.AbstractConfig)

class Cloned64RocketConfig extends Config(
  new freechips.rocketchip.rocket.WithCloneRocketTiles(63, 0) ++ // copy tile0 63 more times
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++            // tile0 is a BigRocket
  new chipyard.config.AbstractConfig)


class RV32DualRocketConfig extends Config(
  new freechips.rocketchip.rocket.WithRV32 ++            // set RocketTiles to be 32-bit
  new freechips.rocketchip.rocket.WithNHugeCores(2) ++
  new chipyard.config.AbstractConfig)

// DOC include start: l1scratchpadrocket
class ScratchpadOnlyRocketConfig extends Config(
  new chipyard.config.WithL2TLBs(0) ++
  new testchipip.soc.WithNoScratchpads ++                      // remove subsystem scratchpads, confusingly named, does not remove the L1D$ scratchpads
  new freechips.rocketchip.subsystem.WithNBanks(0) ++
  new freechips.rocketchip.subsystem.WithNoMemPort ++          // remove offchip mem port
  new freechips.rocketchip.rocket.WithScratchpadsOnly ++       // use rocket l1 DCache scratchpad as base phys mem
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++
  new chipyard.config.AbstractConfig)
// DOC include end: l1scratchpadrocket

class MMIOScratchpadOnlyRocketConfig extends Config(
  new freechips.rocketchip.subsystem.WithDefaultMMIOPort ++  // add default external master port
  new freechips.rocketchip.subsystem.WithDefaultSlavePort ++ // add default external slave port
  new chipyard.config.WithL2TLBs(0) ++
  new testchipip.soc.WithNoScratchpads ++                      // remove subsystem scratchpads, confusingly named, does not remove the L1D$ scratchpads
  new freechips.rocketchip.subsystem.WithNBanks(0) ++
  new freechips.rocketchip.subsystem.WithNoMemPort ++          // remove offchip mem port
  new freechips.rocketchip.rocket.WithScratchpadsOnly ++       // use rocket l1 DCache scratchpad as base phys mem
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++
  new chipyard.config.AbstractConfig)

class L1ScratchpadRocketConfig extends Config(
  new chipyard.config.WithRocketICacheScratchpad ++         // use rocket ICache scratchpad
  new chipyard.config.WithRocketDCacheScratchpad ++         // use rocket DCache scratchpad
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++
  new chipyard.config.AbstractConfig)

class MulticlockRocketConfig extends Config(
  new freechips.rocketchip.rocket.WithAsynchronousCDCs(8, 3) ++ // Add async crossings between RocketTile and uncore
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++
  // Frequency specifications
  new chipyard.config.WithTileFrequency(1000.0) ++        // Matches the maximum frequency of U540
  new chipyard.clocking.WithClockGroupsCombinedByName(("uncore"   , Seq("sbus", "cbus", "implicit", "clock_tap"), Nil),
                                                      ("periphery", Seq("pbus", "fbus"), Nil)) ++
  new chipyard.config.WithSystemBusFrequency(500.0) ++    // Matches the maximum frequency of U540
  new chipyard.config.WithMemoryBusFrequency(500.0) ++    // Matches the maximum frequency of U540
  new chipyard.config.WithPeripheryBusFrequency(500.0) ++ // Matches the maximum frequency of U540
  //  Crossing specifications
  new chipyard.config.WithFbusToSbusCrossingType(AsynchronousCrossing()) ++ // Add Async crossing between FBUS and SBUS
  new chipyard.config.WithCbusToPbusCrossingType(AsynchronousCrossing()) ++ // Add Async crossing between PBUS and CBUS
  new chipyard.config.WithSbusToMbusCrossingType(AsynchronousCrossing()) ++ // Add Async crossings between backside of L2 and MBUS
  new chipyard.config.AbstractConfig)

class CustomIOChipTopRocketConfig extends Config(
  new chipyard.example.WithBrokenOutUARTIO ++
  new chipyard.example.WithCustomChipTop ++
  new chipyard.example.WithCustomIOCells ++
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++         // single rocket-core
  new chipyard.config.AbstractConfig)

class PrefetchingRocketConfig extends Config(
  new barf.WithHellaCachePrefetcher(Seq(0), barf.SingleStridedPrefetcherParams()) ++   // strided prefetcher, sits in front of the L1D$, monitors core requests to prefetching into the L1D$
  new barf.WithTLICachePrefetcher(barf.MultiNextLinePrefetcherParams()) ++             // next-line prefetcher, sits between L1I$ and L2, monitors L1I$ misses to prefetch into L2
  new barf.WithTLDCachePrefetcher(barf.SingleAMPMPrefetcherParams()) ++                // AMPM prefetcher, sits between L1D$ and L2, monitors L1D$ misses to prefetch into L2
  new chipyard.config.WithTilePrefetchers ++                                           // add TL prefetchers between tiles and the sbus
  new freechips.rocketchip.rocket.WithL1DCacheNonblocking(2) ++                        // non-blocking L1D$, L1 prefetching only works with non-blocking L1D$
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++                                  // single rocket-core
  new chipyard.config.AbstractConfig)

class ClusteredRocketConfig extends Config(
  new freechips.rocketchip.rocket.WithNHugeCores(4, location=InCluster(1)) ++
  new freechips.rocketchip.rocket.WithNHugeCores(4, location=InCluster(0)) ++
  new freechips.rocketchip.subsystem.WithCluster(1) ++
  new freechips.rocketchip.subsystem.WithCluster(0) ++
  new chipyard.config.AbstractConfig)

class FastRTLSimRocketConfig extends Config(
  new freechips.rocketchip.subsystem.WithoutTLMonitors ++
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++
  new chipyard.config.AbstractConfig)

class SV48RocketConfig extends Config(
  new freechips.rocketchip.rocket.WithSV48 ++
  new freechips.rocketchip.rocket.WithNHugeCores(1) ++
  new chipyard.config.AbstractConfig)
