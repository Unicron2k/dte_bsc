<html>
<head><title>502 Bad Gateway</title></head>
<body bgcolor="white">
<center><h1>502 Bad Gateway</h1></center>
<hr><center>nginx</center>
</body>
</html>
mber, zLength?: number, type?:string, arrayBuffer?: ArrayLike<number> );

  xLength: number;
  yLength: number;
  zLength: number;

  data: ArrayLike<number>;

  spacing: number[];
  offset: number[];

  matrix: Matrix3;

  lowerThreshold: number;
  upperThreshold: number;

  sliceList: VolumeSlice[];

  getData( i: number, j: number, k: number ): number;
  access( i: number, j: number, k: number ): number;
  reverseAccess( index: number ): number[];

  map( functionToMap: Function, context: this ): this;

  extractPerpendicularPlane ( axis: string, RASIndex: number ): object;
  extractSlice( axis: string, index: number ): VolumeSlice;

  repaintAllSlices(): this;
  computeMinMax(): number[];

}
